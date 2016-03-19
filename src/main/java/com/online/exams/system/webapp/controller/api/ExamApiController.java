package com.online.exams.system.webapp.controller.api;

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

        mongoPaperDao.addPaper(mongoPaper);
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

    @RequestMapping(value = "/programing", method = RequestMethod.POST)
    public JsonResponse programingCheck(@RequestParam("pid") int pid, @RequestParam("qid") int qid, @RequestParam("text") String text) {
        HashMap<String, String> hashMap = getTestCase2HashMap(qid);
        String result = compileJava(text, hashMap);
        if ("0".equals(result)) {
            return JsonResponse.success("代码测试不通过");
        }
        if ("1".equals(result)) {
            Paper paper = paperService.findPaperById(pid);
            MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));
            List<QuestionMap> mapList = mongoPaper.getQuestionMapList();
            for (QuestionMap q : mapList) {
                if (q.getQuestionType() == QuestionTypeEnum.PROGRAMMING_QUESTION) {
                    q.setRight(true);
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
     * 新生成试卷
     */
    @RequestMapping(value = "/generate/{uid}", method = RequestMethod.POST)
    public JsonResponse doPaper(@PathVariable("uid") Integer uid, @RequestParam("questionTagList") String questionTagList, @RequestParam("paperType") String paperType, ModelMap model) {
        Paper paper = paperService.findDoingPaperByUid(uid);
        HashMap<String, Object> hashMap = new HashMap<>();
        if (null != paper) {
            MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));
            hashMap.put("questions", mongoPaper.getQuestionMapList());
            hashMap.put("pid", paper.getId());
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
        answersList = answersList.subSequence(1, answersList.length() - 1).toString();
        String[] answerArray = answersList.split(",");
        for (String s : answerArray) {
            String[] tem = s.split(":");
            answers.put(Integer.parseInt(tem[0]), tem[1]);
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

    private String compileJava(String source, HashMap<String, String> testCase) {
        /**配置参数*/
        String javaexeDir = "C:\\Program Files\\Java\\jdk1.8.0_51\\bin"; //exe文件存放目录
        String delexeDir = "C:\\Windows\\System32"; //exe文件存放目录
        String workDir = "f:\\";     // 工作目录
        String javaSource = "demo.java";    // 需要转换的源文件，于工作目录下
        String javaClass = "demo";    // 需要转换的源文件，于工作目录下
        String testCaseInputFile = "input.txt";    // 测试用例输入，于工作目录下
        String testCaseOutputFile = "output.txt";    // 测试用例输出，于工作目录下
        String testCaseAnswerFile = "answer.txt";    // 测试用例答案，于工作目录下
        String exceptionFile = "exception.txt";    // 异常文件，于工作目录下


        /**清除目录下的文件*/

        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> environment = processBuilder.environment();
        environment.put("Path", environment.get("Path") + delexeDir);
        processBuilder.directory(new File(workDir));
        List<String> command = new LinkedList<>();
        command.add("cmd.exe");
        command.add("/c del /f /q /a f:\\test\\*");

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
            Thread.sleep(6000);
        } catch (Exception e) {
            return "-1";
        }

        /**生成测试文件*/
        File inputGenerate = new File("f:\\input.txt");
        File answerGenerate = new File("f:\\answer.txt");

        try {
            inputGenerate.createNewFile();
            answerGenerate.createNewFile();
            BufferedWriter inputWriter = new BufferedWriter(new FileWriter(inputGenerate));
            BufferedWriter answerWriter = new BufferedWriter(new FileWriter(answerGenerate));

            Iterator iterator = testCase.entrySet().iterator();

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
        } catch (Exception e) {
            return "-1";
        }


        /**输出java代码到目录*/
        File file = new File("f:\\demo.java");
        try {
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(source);
            out.flush();
            out.close();
        } catch (Exception e) {
            return "-1";
        }

        /**设置环境*/
        environment.clear();
        environment.put("Path", environment.get("Path") + javaexeDir);
        processBuilder.directory(new File(workDir));

        command.add("javac.exe");
        command.add(javaSource);
        command.add(">>");
        command.add("exception.txt");
        processBuilder.command(command);

        /**编译java文件*/
        try {
            processBuilder.start();
        } catch (Exception e) {
            return "-1";
        }


        /**检查是否编译出现异常*/
        File exception = new File("f:\\exception.txt");
        if (file.exists()) {
            try {

                FileReader fileExceptionReader = new FileReader(exception);
                BufferedReader exceptionReader = new BufferedReader(fileExceptionReader);

                StringBuffer exceptionStringBuffer = new StringBuffer();
                String es = exceptionReader.readLine();
                while (null != es) {
                    exceptionStringBuffer.append(es);
                    exceptionStringBuffer.append("\r\n");
                    es = exceptionReader.readLine();
                }
                return exceptionStringBuffer.toString();
            } catch (Exception e) {
                return "-1";
            }
        }


        /**环境重置*/
        processBuilder.command().clear();
        command.clear();
        try {
            Thread.sleep(6000);
        } catch (Exception e) {
            return e.toString();
        }

        /**执行demo.java文件*/
        command.add("java.exe");
        command.add(javaClass);
        command.add("<");
        command.add(testCaseInputFile);
        command.add(">>");
        command.add(testCaseOutputFile);

        processBuilder.command(command);
        try {
            processBuilder.start();
        } catch (Exception e) {
            return e.toString();
        }

        /**比较结果文件是否一致*/
        /**java运行文件demo，测试输出是否和结果一致*/

        File fileOutput = new File("f:\\output.txt");
        File fileanswer = new File("f:\\answer.txt");

        try {
            FileReader fileOutputReader = new FileReader(fileOutput);
            FileReader fileanswerReader = new FileReader(fileanswer);
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


    private HashMap<String, String> getTestCase2HashMap(int pid) {
        HashMap<String, String> hashMap = new HashMap<>();
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
