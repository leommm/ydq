package group.ydq.web.controller;

import group.ydq.model.entity.pm.Message;
import group.ydq.model.entity.rbac.User;
import group.ydq.service.service.pm.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author Simple
 * @date on 2018/11/12 22:28
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageServiceImpl messageServiceImpl;

    /*
     * 查询所有
     * 无参，仅供超级管理员查看
     * */
    @GetMapping(value = "/findAll")
    public List<Message> messageList() {
        return messageServiceImpl.messageList();
    }

    /*
     * 单个查询
     * 参数为message.id，仅供超级管理员查看
     * */
    @GetMapping(value = "/messages/{id}")
    Optional<Message> messageOne(@PathVariable("id") Long id) {
        return null;
    }

    /*
     * 新增站内消息
     * 以title、content、time、sender、receivers、remark为参数
     * */
    @GetMapping(value = "/addOne")
    public Message sendMessage(Message messageOne) {
        return messageServiceImpl.sendMessage(messageOne);
    }

    /*
     * 根据id删除message
     *
     * */
    @GetMapping(value = "/deleteOne/{id}")
    public void deleteOne(Long id) {
        messageServiceImpl.delete(id);
    }

    /*
     * 查询所有消息
     * 以寄件人为参数
     * */
    @GetMapping(value = "/messageBySender/{sender}")
    public List<Message> findBySender(User sender, int page, int limit) {
        return messageServiceImpl.findBySender(sender, page, limit);
    }

    /*
     * 查询所有消息
     * 以收件人为参数
     * */
    @GetMapping(value = "/messageByReceiver/{receiver}")
    public List<Message> findByReceiver(User receiver, int page, int limit) {
        return messageServiceImpl.findByReceiver(receiver, page, limit);
    }

    public static void main(String[] args) {
        System.out.println(new Random().nextInt(10));
    }

}
