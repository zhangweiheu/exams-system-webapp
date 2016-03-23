package com.online.exams.system.webapp.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.MongoPaper;
import com.online.exams.system.core.bean.QuestionMap;
import com.online.exams.system.core.bean.TestCase;
import com.online.exams.system.core.dao.MongoPaperDao;
import com.online.exams.system.core.dao.MongoTestCaseDao;
import com.online.exams.system.core.enums.QuestionTypeEnum;
import com.online.exams.system.core.enums.RefTypeEnum;
import com.online.exams.system.core.enums.StatusEnum;
import com.online.exams.system.core.enums.TagEnum;
import com.online.exams.system.core.model.Paper;
import com.online.exams.system.core.service.PaperGenerateService;
import com.online.exams.system.core.service.PaperService;
import com.online.exams.system.core.service.TagService;
import com.online.exams.system.core.util.PaperUtil;
import com.online.exams.system.webapp.annotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

/**
 * Created by zhang on 2016/3/8.
 */
@RestController
@LoginRequired
@RequestMapping("/api/system/exam")
public class ExamApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamApiController.class);

    @Autowired
    PaperService paperService;

    @Autowired
    MongoPaperDao mongoPaperDao;

    @Autowired
    MongoTestCaseDao mongoTestCaseDao;

    @Autowired
    PaperGenerateService paperGenerateService;

    @Autowired
    TagService tagService;

    /**
     * 做题
     */
    @RequestMapping(value = "/do/{pid}", method = RequestMethod.PUT)
    public JsonResponse updatePaperById(@RequestParam("answersList") String answersList, @PathVariable("pid") Integer pid) {
        HashMap<Integer, String> newAnswers = convertAnswersListString2AnswersMap(answersList);
        Paper paper = paperService.findPaperById(pid);
        if (paper.getStatus() == StatusEnum.CLOSE || paper.getStatus() == StatusEnum.DELETE) {
            return JsonResponse.failed("试卷不可修改");
        }
        MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));

        List<QuestionMap> questionMaps = mongoPaper.getQuestionMapList();
        for (QuestionMap qMap : questionMaps) {
            if (newAnswers.containsKey(qMap.getId())) {
                qMap.setCurrentAnswer(newAnswers.get(qMap.getId()));
                qMap.setRight(qMap.getAnswers().equals(qMap.getCurrentAnswer()));
            }
        }
        mongoPaper.setQuestionMapList(questionMaps);

        paper.setScore(PaperUtil.calautePaperScore(questionMaps));
        paper.setTotalRight(PaperUtil.calautePaperTotalSuccess(questionMaps));
        paperService.updatePaper(paper);

        mongoPaperDao.updateMongoPaper(mongoPaper);
        return JsonResponse.success();
    }

    @RequestMapping(value = "/close/{uid}/{pid}", method = RequestMethod.PUT)
    public JsonResponse closeExam(@PathVariable("uid") int uid, @PathVariable("pid") int pid) {
        Paper paper = new Paper();
        paper.setId(pid);
        paper.setUserId(uid);
        paper.setStatus(StatusEnum.CLOSE);
        return JsonResponse.success(paperService.updatePaper(paper));
    }

    @RequestMapping(value = "/programing", method = RequestMethod.PUT)
    public JsonResponse programingCheck(@RequestParam("pid") int pid, @RequestParam("qid") int qid, @RequestParam("text") String text) {
        LinkedHashMap<String, String> hashMap = getTestCase2HashMap(pid);
        String result = compileJava(text, hashMap);
        if ("0".equals(result)) {
            return JsonResponse.success("代码测试不通过");
        }
        if ("1".equals(result)) {
            Paper paper = paperService.findPaperById(pid);
            MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));
            List<QuestionMap> mapList = mongoPaper.getQuestionMapList();
            for (QuestionMap q : mapList) {
                if (q.getQuestionType() == QuestionTypeEnum.PROGRAMMING_QUESTION && q.getId() == qid) {
                    q.setRight(true);
                    q.setCurrentAnswer(text);
                    break;
                }
            }
            mongoPaper.setQuestionMapList(mapList);
            mongoPaperDao.updateMongoPaper(mongoPaper);

            paper.setScore(PaperUtil.calautePaperScore(mapList));
            paper.setTotalRight(PaperUtil.calautePaperTotalSuccess(mapList));
            paperService.updatePaper(paper);

            return JsonResponse.success("代码测试通过");
        }
        if ("-1".equals(result)) {
            return JsonResponse.success("系统出现异常！请联系管理员！");
        }
        return JsonResponse.success(result);
    }

    /**
     * 检查是否存在未完成试卷，如果有，则返回，否则返回fail
     */
    @RequestMapping(value = "/check/{uid}", method = RequestMethod.GET)
    public JsonResponse checkHaveDoingPaper(@PathVariable("uid") Integer uid) {
        Paper paper = paperService.findDoingPaperByUid(uid);
        if (null != paper) {
            MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));
            JsonResponse jsonResponse = JsonResponse.success();
            jsonResponse.put("uid", uid);
            List<QuestionMap> list = mongoPaper.getQuestionMapList();
            for (QuestionMap questionMap : list) {
                questionMap.setRight(null);
                questionMap.setAnswers(null);
            }
            jsonResponse.put("questions", list);
            jsonResponse.put("pid", paper.getId());

            return jsonResponse;
        } else {
            return JsonResponse.failed();
        }
    }

    @RequestMapping(value = "/generate/{uid}", method = RequestMethod.POST)
    public JsonResponse generatePaper(@PathVariable("uid") Integer uid, @RequestParam("questionTagList") String questionTagList, @RequestParam("paperType") String paperType, ModelMap model) {
        Paper paper = paperService.findDoingPaperByUid(uid);
        HashMap<String, Object> hashMap;
        if (null != paper) {
            return JsonResponse.failed("存在未完成试卷，请先完成！");
        } else {
            List<TagEnum> tagEnumList = convertTagLisString2TagList(questionTagList);
            switch (paperType) {
                case "SINGLE_SELECTION":
                    hashMap = paperGenerateService.generateSingleSelection(tagEnumList, uid);
                    break;
                case "MULTI_SELECTION":
                    hashMap = paperGenerateService.generateMultiSelection(tagEnumList, uid);
                    break;
                case "PROGRAMMING_QUESTION":
                    hashMap = paperGenerateService.generateProgrammingQuestion(tagEnumList, uid);
                    break;
                case "SINGLE_AND_MULTI":
                    hashMap = paperGenerateService.generateSingleMultiSelection(tagEnumList, uid);
                    break;
                case "SINGLE_AND_PROGRAMMING":
                    hashMap = paperGenerateService.generateSingleProgrammingQuestion(tagEnumList, uid);
                    break;
                case "MULTI_AND_PROGRAMMING":
                    hashMap = paperGenerateService.generateMultiProgrammingSelection(tagEnumList, uid);
                    break;
                case "SINGLE_AND_MULTI_PROGRAMMING":
                    hashMap = paperGenerateService.generateSingleMultiProgrammingQuestion(tagEnumList, uid);
                    break;
                default:
                    hashMap = paperGenerateService.generateSingleMultiProgrammingQuestion(tagEnumList, uid);
                    break;
            }
            tagService.saveTagList(questionTagList, (Integer) hashMap.get("pid"), RefTypeEnum.PAPER);
        }

        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.put("uid", uid);
        jsonResponse.put("questions", hashMap.get("questions"));
        jsonResponse.put("pid", hashMap.get("pid"));

        return jsonResponse;
    }

    private HashMap<Integer, String> convertAnswersListString2AnswersMap(String answersList) {
        HashMap<Integer, String> answers = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(answersList);

        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> param = it.next();
            answers.put(Integer.parseInt(param.getKey().toString()), param.getValue().toString());
        }
        return answers;
    }

    private List<TagEnum> convertTagLisString2TagList(String enumList) {
        List<TagEnum> tagEnumList = new ArrayList<>();
        if (StringUtils.isEmpty(enumList)) {
            return tagEnumList;
        }
        String[] chars = enumList.split(",");
        for (String c : chars) {
            tagEnumList.add(TagEnum.valueOf(c));
        }
        return tagEnumList;
    }

    private String compileJava(String source, LinkedHashMap<String, String> testCase) {
        /**配置参数*/
        String javaexeDir = "C:\\Program Files\\Java\\jdk1.8.0_51\\bin"; //exe文件存放目录
        String delexeDir = "C:\\Windows\\System32"; //exe文件存放目录
        String workDir = "f:\\";     // 工作目录
        String javaSource = "f:\\demo.java";    // 需要转换的源文件，于工作目录下
        String javaClass = "demo";    // 需要转换的源文件，于工作目录下
        String inputFilePath = "f:\\input.txt";    // 测试用例输入，于工作目录下
        String outputFilePath = "f:\\output.txt";    // 测试用例输出，于工作目录下
        String answerFilePath = "f:\\answer.txt";    // 测试用例答案，于工作目录下
        String exceptionFilePath = "f:\\exception.txt";    // 异常文件，于工作目录下


        /**清除目录下的文件*/

        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> environment = processBuilder.environment();
        environment.put("Path", environment.get("Path") + delexeDir);
        processBuilder.directory(new File(workDir));
        List<String> command = new LinkedList<>();
        command.add("cmd.exe");
        command.add("/c del /f /q /a f:\\*");

        processBuilder.command(command);

        try {
            processBuilder.start();
        } catch (Exception e) {
            return "-1";
        }

        /**环境重置*/
        processBuilder.command().clear();
        command.clear();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            return "-1";
        }

        /**生成测试文件*/
        Boolean isInputExist = false;
        try {
            Iterator iterator = testCase.entrySet().iterator();
            Map.Entry entry1 = (Map.Entry) iterator.next();

            if (!entry1.getKey().toString().matches("^print.*")) {

                File inputGenerate = new File(inputFilePath);
                File answerGenerate = new File(answerFilePath);
                BufferedWriter inputWriter = new BufferedWriter(new FileWriter(inputGenerate));
                BufferedWriter answerWriter = new BufferedWriter(new FileWriter(answerGenerate));
                isInputExist = true;
                iterator = testCase.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    inputWriter.write(entry.getKey() + "\r\n");
                    answerWriter.write(entry.getValue() + "\r\n");
                    iterator.remove();
                }

                inputWriter.flush();
                answerWriter.flush();
                inputWriter.close();
                answerWriter.close();
            } else {

                File answerGenerate = new File(answerFilePath);
                BufferedWriter answerWriter = new BufferedWriter(new FileWriter(answerGenerate));
                iterator = testCase.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    answerWriter.write(entry.getValue() + "\r\n");
                    iterator.remove();
                }
                answerWriter.flush();
                answerWriter.close();
            }
        } catch (Exception e) {
            return "-1";
        }

        /**输出java代码到目录*/
        File file = new File(javaSource);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(source);
            out.flush();
            out.close();
        } catch (Exception e) {
            return "-1";
        }

        /**设置环境*/

        command.add("cmd.exe");
        command.add("/c javac demo.java >> exception.txt");
        processBuilder.command(command);

        File exceptionFile = new File(exceptionFilePath);
        /**编译java文件*/
        try {
            processBuilder.start();
        } catch (Exception e) {
            return "-1";
        }

        /**检查是否编译出现异常*/
        if(exceptionFile.exists()){
            try {

                FileReader fileExceptionReader = new FileReader(exceptionFile);
                BufferedReader exceptionReader = new BufferedReader(fileExceptionReader);

                StringBuffer exceptionStringBuffer = new StringBuffer();
                String es = exceptionReader.readLine();
                while (null != es) {
                    exceptionStringBuffer.append(es);
                    exceptionStringBuffer.append("\r\n");
                    es = exceptionReader.readLine();
                }
                String exceptionResult = exceptionStringBuffer.toString();
                if (!"".equals(exceptionResult.trim())) {
                    return exceptionResult.trim();
                }
            } catch (Exception e) {
                return "-1";
            }
        }



        /**环境重置*/
        processBuilder.command().clear();
        command.clear();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            return e.toString();
        }

        /**执行编译demo.java文件*/
        command.add("cmd.exe");

        if (isInputExist) {
            command.add("/c java demo < input.txt >> output.txt");
        }else{
            command.add("/c java demo >> output.txt");
        }

        processBuilder.command(command);
        try {
            processBuilder.start();
        } catch (Exception e) {
            return e.toString();
        }

        /**比较结果文件是否一致*/
        /**java运行文件demo，测试输出是否和结果一致*/

        try {
            FileReader fileOutputReader = new FileReader(outputFilePath);
            FileReader fileanswerReader = new FileReader(answerFilePath);
            BufferedReader outputReader = new BufferedReader(fileOutputReader);
            BufferedReader answerReader = new BufferedReader(fileanswerReader);

            StringBuffer answerStringBuffer = new StringBuffer();
            StringBuffer outputStringBuffer = new StringBuffer();
            String answer = answerReader.readLine();
            String output = outputReader.readLine();

            while (null != answer) {
                if (!answer.equals(output)) {
                    return "0";
                }
                answerStringBuffer.append(answer);
                answer = answerReader.readLine();
                outputStringBuffer.append(output);
                output = outputReader.readLine();
            }
            while (null != output) {
            }
            if (answerStringBuffer.length() != outputStringBuffer.length()) {
                return "0";
            }
            String a = answerStringBuffer.toString();
            String o = outputStringBuffer.toString();
            if (!a.equals(o)) {
                return o;
            }
        } catch (Exception e) {
            return "-1";
        }

        return "1";
    }

    private LinkedHashMap<String, String> getTestCase2HashMap(int pid) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        Paper paper = paperService.findPaperById(pid);
        MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));
        List<QuestionMap> mapList = mongoPaper.getQuestionMapList();
        Long tcid = 0L;
        for (QuestionMap q : mapList) {
            if (q.getQuestionType() == QuestionTypeEnum.PROGRAMMING_QUESTION) {
                tcid = Integer.toUnsignedLong(Integer.valueOf(q.getAnswers()));
                break;
            }
        }
        TestCase testCase = mongoTestCaseDao.findTestCaseById(tcid);
        return testCase.getKeyValue();
    }

}
