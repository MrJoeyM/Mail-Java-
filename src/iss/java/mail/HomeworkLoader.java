package iss.java.mail;

import org.reflections.Reflections;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Random;
import java.util.Set;


public class HomeworkLoader {
    public static void main(String[] args) throws MessagingException, IOException, InterruptedException, IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections("iss.java.mail");
        Set<Class<? extends IMailService>> impls = reflections.getSubTypesOf(IMailService.class);
        Class<? extends IMailService> homework = impls.iterator().next();
        IMailService service = homework.newInstance();
        String name = homework.getSimpleName();
        String id = name.substring(name.length()-17);
        System.out.println(id);
        service.connect();
        int random = new Random().nextInt(3);
        System.out.println("mail ID: "+random);
       service.send("issjava2015@foxmail.com", "java作业"+random+"_"+id,"测试");
        System.out.println("mail sent");
        for(int i = 0;i<30;i++){
            if(service.listen()){
                System.out.println(service.getReplyMessageContent("issjava2015@163.com","自动回复：java作业"+random+"_"+id));
                System.out.println("check if your mail id and verify id match");
                return;
            }
            System.out.println("waiting for auto reply");
            Thread.sleep(5000);
        }
        System.out.println("time out");
    }
}
