package com.viroyal.doormagnet.modules.system.model;

public class SysAnnouncementWithBLOBs extends SysAnnouncement {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_announcement.msg_content
     *
     * @mbg.generated Wed Mar 25 10:01:15 CST 2020
     */
    private String msgContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_announcement.user_ids
     *
     * @mbg.generated Wed Mar 25 10:01:15 CST 2020
     */
    private String userIds;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_announcement.msg_content
     *
     * @return the value of sys_announcement.msg_content
     *
     * @mbg.generated Wed Mar 25 10:01:15 CST 2020
     */
    public String getMsgContent() {
        return msgContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_announcement.msg_content
     *
     * @param msgContent the value for sys_announcement.msg_content
     *
     * @mbg.generated Wed Mar 25 10:01:15 CST 2020
     */
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_announcement.user_ids
     *
     * @return the value of sys_announcement.user_ids
     *
     * @mbg.generated Wed Mar 25 10:01:15 CST 2020
     */
    public String getUserIds() {
        return userIds;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_announcement.user_ids
     *
     * @param userIds the value for sys_announcement.user_ids
     *
     * @mbg.generated Wed Mar 25 10:01:15 CST 2020
     */
    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }
}