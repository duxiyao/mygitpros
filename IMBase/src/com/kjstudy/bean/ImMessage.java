package com.kjstudy.bean;

import org.kymjs.kjframe.database.annotate.Transient;

import com.kjstudy.a.im.AMsgView;

public class ImMessage extends Entity {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;
	private int id;

	/**
	 * 1 left;2 right;3 center
	 */
	private int msgLocationType;
	private String msgBody;
	/**
	 * 消息类型 , 文本消息、媒体消息等 IMTextView
	 */
	private String msgViewClass;

	private String msgId;

	private String dateCreate;
	private String dateRece;
	private String dateSend;
	private String senderName;
	private String mobileNum;
	private String voipAccount;
	private String userData;
	private String fileUrl;
	private String filePath;
	private String fileExt;
	/**
	 * 1  正在发送    2  发送成功   3  发送失败
	 */
	private int sendState;
	private int readState;
	private String duration;
	private int attachmentState;
	private String fileSize;

	public String getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getDateRece() {
		return dateRece;
	}

	public void setDateRece(String dateRece) {
		this.dateRece = dateRece;
	}

	public String getDateSend() {
		return dateSend;
	}

	public void setDateSend(String dateSend) {
		this.dateSend = dateSend;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getVoipAccount() {
		return voipAccount;
	}

	public void setVoipAccount(String voipAccount) {
		this.voipAccount = voipAccount;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public int getSendState() {
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public int getReadState() {
		return readState;
	}

	public void setReadState(int readState) {
		this.readState = readState;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getAttachmentState() {
		return attachmentState;
	}

	public void setAttachmentState(int attachmentState) {
		this.attachmentState = attachmentState;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Class<? extends AMsgView> getMsgType() {
		try {
			Class<? extends AMsgView> obj = (Class<? extends AMsgView>) Class
					.forName(msgViewClass);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsgViewClass() {
		return msgViewClass;
	}

	public void setMsgViewClass(String msgViewClass) {
		this.msgViewClass = msgViewClass;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	/**
	 * @return 1 left;2 right;3 center
	 */
	public int getMsgLocationType() {
		return msgLocationType;
	}

	/**
	 * @param msgLocationType 1 left;2 right;3 center
	 */
	public void setMsgLocationType(int msgLocationType) {
		this.msgLocationType = msgLocationType;
	}
}
