package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, User> adminMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;

    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository() {
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        if (userMobile.contains(mobile)) {
            throw new Exception();
        }
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        if (users.size() == 2) {
            String grpName = users.get(1).getName();
            Group group = new Group(grpName, users.size());
            groupUserMap.put(group, users);
            User adm = users.get(0);
            adminMap.put(group, adm);
            return group;
        } else if (users.size() > 2) {
            String grpName = "Group " + customGroupCount + 1;
            customGroupCount++;
            Group group = new Group(grpName, users.size());
            groupUserMap.put(group, users);
            User adm = users.get(0);
            adminMap.put(group, adm);
            return group;
        }
        return null;
    }

    public int createMessage(String content) {
        messageId++;
        Message m = new Message(messageId, content);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if (!groupUserMap.containsKey(group)) {
            throw new Exception();
        }

        List<User> guserList = groupUserMap.get(group);

        List<Message> messageList;

        int messageCount = 0;
        boolean userExist = false;
        for (User user : guserList) {
            if (user.getMobile().equals(sender.getMobile())) {
                userExist = true;
                senderMap.put(message, sender);
                if (groupMessageMap.containsKey(group)) {
                    messageList = groupMessageMap.get(group);
                } else {
                    messageList = new ArrayList<>();
                }
                messageList.add(message);
                messageCount = messageList.size();
                groupMessageMap.put(group, messageList);
            }
        }
        if (!userExist) {
            throw new Exception();
        } else {
            return messageCount;
        }
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if (!groupUserMap.containsKey(group)) {
            throw new Exception();
        }

        boolean userExist = false;
        if (adminMap.containsKey(group)) {
            User currAdmin = adminMap.get(group);
            if (!currAdmin.getMobile().equals(approver.getMobile())) {
                throw new Exception();
            } else {
                List<User> guserList = groupUserMap.get(group);
                for (User usr : guserList) {
                    if (usr.getMobile().equals(user.getMobile())) {
                        adminMap.put(group, user);
                        userExist = true;
                    }
                }
            }
        }
        if (!userExist) throw new Exception();
        else return "SUCCESS";
    }

    public int removeUser(User user) {
        return 0;
    }

    public String findMessage(Date start, Date end, int k) {
       return "SUCESS";
    }
}
