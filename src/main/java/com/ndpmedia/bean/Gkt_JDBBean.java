package com.ndpmedia.bean;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;

/**
 * @ClassName: Gkt_JDBBean
 * @Description: Gkt_JDBBean EntityClass
 * @author ivan.wang
 * 
 */ 

public class Gkt_JDBBean{
	private int id;
	private String productTeam;
	private String globalStatus;
	private String creator;
	private String environment;
	private String build_num;
	private String build_version;
	private String publish_image_to_aws;
	private String components;
	private String offlineImages;
	private String onlineImages;
	private String application_name;
	private String deploy_status;
	private String stack_list;
	private String resource_list;
	private Timestamp creation_time;
	private Timestamp end_time;
	private Timestamp tar_start_time;
	private Timestamp tar_end_time;
	private String tar_complete_flag;
	private Timestamp image_start_time;
	private Timestamp image_end_time;
	private String image_complete_flag;
	private Timestamp deploy_start_time;
	private Timestamp deploy_end_time;
	private String deploy_complete_flag;
	private String build_id;
	private String job_name;
	private String sourceCode;
	private String tar_notbuild_flag;
	private String log;
	private String base_build_num;
	private String base_image_version;

	public Gkt_JDBBean() {
	}

	public Gkt_JDBBean beanId(int id){
		this.id = id;
		return this;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public Gkt_JDBBean beanProductTeam(String productTeam){
		this.productTeam = productTeam;
		return this;
	}

	public String getProductTeam(){
		return productTeam;
	}

	public void setProductTeam(String productTeam){
		this.productTeam = productTeam;
	}

	public Gkt_JDBBean beanGlobalStatus(String globalStatus){
		this.globalStatus = globalStatus;
		return this;
	}

	public String getGlobalStatus(){
		return globalStatus;
	}

	public void setGlobalStatus(String globalStatus){
		this.globalStatus = globalStatus;
	}

	public Gkt_JDBBean beanCreator(String creator){
		this.creator = creator;
		return this;
	}

	public String getCreator(){
		return creator;
	}

	public void setCreator(String creator){
		this.creator = creator;
	}

	public Gkt_JDBBean beanEnvironment(String environment){
		this.environment = environment;
		return this;
	}

	public String getEnvironment(){
		return environment;
	}

	public void setEnvironment(String environment){
		this.environment = environment;
	}

	public Gkt_JDBBean beanBuild_num(String build_num){
		this.build_num = build_num;
		return this;
	}

	public String getBuild_num(){
		return build_num;
	}

	public void setBuild_num(String build_num){
		this.build_num = build_num;
	}

	public Gkt_JDBBean beanBuild_version(String build_version){
		this.build_version = build_version;
		return this;
	}

	public String getBuild_version(){
		return build_version;
	}

	public void setBuild_version(String build_version){
		this.build_version = build_version;
	}

	public Gkt_JDBBean beanPublish_image_to_aws(String publish_image_to_aws){
		this.publish_image_to_aws = publish_image_to_aws;
		return this;
	}

	public String getPublish_image_to_aws(){
		return publish_image_to_aws;
	}

	public void setPublish_image_to_aws(String publish_image_to_aws){
		this.publish_image_to_aws = publish_image_to_aws;
	}

	public Gkt_JDBBean beanComponents(String components){
		this.components = components;
		return this;
	}

	public String getComponents(){
		return components;
	}

	public void setComponents(String components){
		this.components = components;
	}

	public String getOfflineImages() {
        return offlineImages;
    }

    public void setOfflineImages(String offlineImages) {
        this.offlineImages = offlineImages;
    }
    
    public Gkt_JDBBean beanOfflineImages(String offlineImages) {
        this.offlineImages = offlineImages;
        return this;
    }

    public String getOnlineImages() {
        return onlineImages;
    }

    public void setOnlineImages(String onlineImages) {
        this.onlineImages = onlineImages;
    }
    
    public Gkt_JDBBean beanOnlineImages(String onlineImages) {
        this.onlineImages = onlineImages;
        return this;
    }

    public Gkt_JDBBean beanApplication_name(String application_name){
		this.application_name = application_name;
		return this;
	}

	public String getApplication_name(){
		return application_name;
	}

	public void setApplication_name(String application_name){
		this.application_name = application_name;
	}

	public Gkt_JDBBean beanDeploy_status(String deploy_status){
		this.deploy_status = deploy_status;
		return this;
	}

	public String getDeploy_status(){
		return deploy_status;
	}

	public void setDeploy_status(String deploy_status){
		this.deploy_status = deploy_status;
	}

	public Gkt_JDBBean beanStack_list(String stack_list){
		this.stack_list = stack_list;
		return this;
	}

	public String getStack_list(){
		return stack_list;
	}

	public void setStack_list(String stack_list){
		this.stack_list = stack_list;
	}

	public Gkt_JDBBean beanResource_list(String resource_list){
		this.resource_list = resource_list;
		return this;
	}

	public String getResource_list(){
		return resource_list;
	}

	public void setResource_list(String resource_list){
		this.resource_list = resource_list;
	}

	public Gkt_JDBBean beanCreation_time(Timestamp creation_time){
		this.creation_time = creation_time;
		return this;
	}

	public Timestamp getCreation_time(){
		return creation_time;
	}

	public void setCreation_time(Timestamp creation_time){
		this.creation_time = creation_time;
	}

	public Gkt_JDBBean beanEnd_time(Timestamp end_time){
		this.end_time = end_time;
		return this;
	}

	public Timestamp getEnd_time(){
		return end_time;
	}

	public void setEnd_time(Timestamp end_time){
		this.end_time = end_time;
	}

	public Gkt_JDBBean beanTar_start_time(Timestamp tar_start_time){
		this.tar_start_time = tar_start_time;
		return this;
	}

	public Timestamp getTar_start_time(){
		return tar_start_time;
	}

	public void setTar_start_time(Timestamp tar_start_time){
		this.tar_start_time = tar_start_time;
	}

	public Gkt_JDBBean beanTar_end_time(Timestamp tar_end_time){
		this.tar_end_time = tar_end_time;
		return this;
	}

	public Timestamp getTar_end_time(){
		return tar_end_time;
	}

	public void setTar_end_time(Timestamp tar_end_time){
		this.tar_end_time = tar_end_time;
	}

	public Gkt_JDBBean beanTar_complete_flag(String tar_complete_flag){
		this.tar_complete_flag = tar_complete_flag;
		return this;
	}

	public String getTar_complete_flag(){
		return tar_complete_flag;
	}

	public void setTar_complete_flag(String tar_complete_flag){
		this.tar_complete_flag = tar_complete_flag;
	}

	public Gkt_JDBBean beanImage_start_time(Timestamp image_start_time){
		this.image_start_time = image_start_time;
		return this;
	}

	public Timestamp getImage_start_time(){
		return image_start_time;
	}

	public void setImage_start_time(Timestamp image_start_time){
		this.image_start_time = image_start_time;
	}

	public Gkt_JDBBean beanImage_end_time(Timestamp image_end_time){
		this.image_end_time = image_end_time;
		return this;
	}

	public Timestamp getImage_end_time(){
		return image_end_time;
	}

	public void setImage_end_time(Timestamp image_end_time){
		this.image_end_time = image_end_time;
	}

	public Gkt_JDBBean beanImage_complete_flag(String image_complete_flag){
		this.image_complete_flag = image_complete_flag;
		return this;
	}

	public String getImage_complete_flag(){
		return image_complete_flag;
	}

	public void setImage_complete_flag(String image_complete_flag){
		this.image_complete_flag = image_complete_flag;
	}

	public Gkt_JDBBean beanDeploy_start_time(Timestamp deploy_start_time){
		this.deploy_start_time = deploy_start_time;
		return this;
	}

	public Timestamp getDeploy_start_time(){
		return deploy_start_time;
	}

	public void setDeploy_start_time(Timestamp deploy_start_time){
		this.deploy_start_time = deploy_start_time;
	}

	public Gkt_JDBBean beanDeploy_end_time(Timestamp deploy_end_time){
		this.deploy_end_time = deploy_end_time;
		return this;
	}

	public Timestamp getDeploy_end_time(){
		return deploy_end_time;
	}

	public void setDeploy_end_time(Timestamp deploy_end_time){
		this.deploy_end_time = deploy_end_time;
	}

	public Gkt_JDBBean beanDeploy_complete_flag(String deploy_complete_flag){
		this.deploy_complete_flag = deploy_complete_flag;
		return this;
	}

	public String getDeploy_complete_flag(){
		return deploy_complete_flag;
	}

	public void setDeploy_complete_flag(String deploy_complete_flag){
		this.deploy_complete_flag = deploy_complete_flag;
	}

	public Gkt_JDBBean beanBuild_id(String build_id){
		this.build_id = build_id;
		return this;
	}

	public String getBuild_id(){
		return build_id;
	}

	public void setBuild_id(String build_id){
		this.build_id = build_id;
	}

	public Gkt_JDBBean beanJob_name(String job_name){
		this.job_name = job_name;
		return this;
	}

	public String getJob_name(){
		return job_name;
	}

	public void setJob_name(String job_name){
		this.job_name = job_name;
	}

	public Gkt_JDBBean beanSourceCode(String sourceCode){
		this.sourceCode = sourceCode;
		return this;
	}

	public String getSourceCode(){
		return sourceCode;
	}

	public void setSourceCode(String sourceCode){
		this.sourceCode = sourceCode;
	}

	public Gkt_JDBBean beanTar_notbuild_flag(String tar_notbuild_flag){
		this.tar_notbuild_flag = tar_notbuild_flag;
		return this;
	}

	public String getTar_notbuild_flag(){
		return tar_notbuild_flag;
	}

	public void setTar_notbuild_flag(String tar_notbuild_flag){
		this.tar_notbuild_flag = tar_notbuild_flag;
	}

	public Gkt_JDBBean beanLog(String log){
		this.log = log;
		return this;
	}

	public String getLog(){
		return log;
	}

	public void setLog(String log){
		this.log = log;
	}

	public Gkt_JDBBean beanBase_build_num(String base_build_num){
		this.base_build_num = base_build_num;
		return this;
	}

	public String getBase_build_num(){
		return base_build_num;
	}

	public void setBase_build_num(String base_build_num){
		this.base_build_num = base_build_num;
	}

	public Gkt_JDBBean beanBase_image_version(String base_image_version){
		this.base_image_version = base_image_version;
		return this;
	}

	public String getBase_image_version(){
		return base_image_version;
	}

	public void setBase_image_version(String base_image_version){
		this.base_image_version = base_image_version;
	}
}

