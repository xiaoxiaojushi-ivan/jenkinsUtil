var json;
var currentPage=1;
var pageCapacity=6;
var creator="";
var env="";
var sumCount = 0;
//记录上次选中行标
var select=0;

var autoRefresh=false;
var refreshInt;
var logPanelOpen=false;

$(document).ready(function() { 
    $("#creator").val('');
    $("#env option:first").prop("selected", 'selected');
    var url=$.ajaxSettings.url;
    var parm=url.split('?')[1];
    if (parm) {
        var map=parm.split('&');
        for ( var item in map) {
            var varry = map[item].split('=');
            if (varry[0] == "env") {
                env= varry[1];
                $("#env").val(env);
            }
        }
    }
    getEnvs();
    getFbDB();
    pageCode();
});
function getEnvs(){
    $.ajax({
        type : "get",
        async:false,
        url : "ymDB/getEnvs",
        dataType : "json",
        beforeSend: LoadFunction,
        error: erryFunction,
        success: succFunction
    });
    function LoadFunction() {  
        $("#basic").html('加载中...');  
    }
    function erryFunction() {  
        alert("error");  
    }
    function succFunction(res) {
        $("#env").html("<option value=\"\" selected=\"selected\">all</option>");
        var datas = res.data;
        $.each(datas, function (i, data) {
            $("#env").html($("#env").html()
                    +"<option value="+data+">"+data+"</option>"
            );
        });
    }
}
function getFbDB(){
    $.ajax({
        type : "get",
        async:false,
        url : "ymDB/getYmDB?page="+currentPage+"&pageCapacity="+pageCapacity+"&creator="+creator+"&environment="+env+"&sortName=creation_time&sortType=desc",
        dataType : "json",
        beforeSend: LoadFunction,
        error: erryFunction,
        success: succFunction
    });
    function LoadFunction() {  
        $("#basic").html('加载中...');  
    }
    function erryFunction() {  
        alert("error");  
    }
    function succFunction(res) {
        json = res;
        var datas = json.data;
        $("#basic").html('');
        sumCount = res.total;
        $("#sumCount").html("查询结果:<font color=green>"+sumCount+"</font>条");
        $.each(datas, function (i, data) {
            // 循环获取数据
            var id = data.id;
            var globalStatus = data.globalStatus;
            var creator = data.creator;
            var environment = data.environment;
            var sourceCode = undefinedFormat(data.sourceCode);
            var build_num = data.build_num;
            var base_build_num = undefinedFormat(data.base_build_num);
            var base_image_version = undefinedFormat(data.base_image_version);
            var components = splitComponents(data.components); 
            var firstCom = firstComponents(data.components);
            var components_size = componentsSize(data.components); 
            
            var images = splitImages(data.images);
            var firstImage = getFirstImage(data.images);
            
            var pushImagesToAWS = splitImages(data.onlineImages);
            var firstImageAWS = getFirstImage(data.onlineImages);
            
            var creation_time = data.creation_time;
            var end_time = data.end_time;
            var interval = timeInterval(creation_time,end_time);
            
            var application_name = undefinedFormat(data.application_name);
            var stack_list = data.stack_list;
            var stack_status = "";
            if(stack_list){
                stack_list = eval("(" + stack_list + ")");
                $.each(stack_list, function (i, data) {
                    var stack_name = data.stack_name;
                    if(stack_name != application_name){
                        return true;
                    }
                    stack_status = data.stack_status;
                });
            }
            
            $("#basic").html($("#basic").html()
            +"<tr id='tr"+i+"' onclick=\"redioFun("+i+")\">"
                +"<td><i id=\"id"+i+"\" class=\"icon icon-circle-thin\"></i></td>"
                +"<td class='globalstatus'>"+globalStatus+"</td>"
                +"<td>"+id+"</td>"
                +"<td>"+creator+"</td>"
                +"<td>"+environment+"</td>"
                +"<td>"+build_num+"</td>"
                +"<td><span class=\"mytooltip\" data-html=\"true\" title=\"<div style='text-align:left;' >" +
                        components +
                        "</div>\" data-placement=\"right\" data-toggle=\"tooltip\">" +
                        firstCom +
                        "</span>"
                +"</td>"
                +"<td><a href=\"http://172.30.10.171/YeahMobi/WAJ/\">repository</a></td>"
                +"<td><span class=\"mytooltip\" data-html=\"true\" date-trigger=\"manual\" title=\"<div style='text-align:left;' >" +
                        images +
                        "</div>\" data-placement=\"right\" data-toggle=\"tooltip\">" +
                        firstImage +
                        "</span>"
                +"</td>"
                +"<td><span class=\"mytooltip\" data-html=\"true\" date-trigger=\"manual\" title=\"<div style='text-align:left;' >" +
                        pushImagesToAWS +
                        "</div>\" data-placement=\"right\" data-toggle=\"tooltip\">" +
                        firstImageAWS +
                        "</span>"
                +"</td>"
                +"<td>"+timeFormat(creation_time)+"</td>"
                +"<td>"+interval+"</td>"
                +"<td>"+application_name+"</td>"
                +"<td>"+stack_status+"</td>"
            +"</tr>"
            );
        });
        redioFun(select);
        booleanIcon();
//        dockerDeployStats(index);
        globalStatus();
        
        $('.mytooltip').tooltip();
    }
}
function undefinedFormat(str){
    if (!str) {
        return "";
    }
    return str;
}
function toDecimal(x) {    
    var f = parseFloat(x);    
    if (isNaN(f)) {    
        return;    
    }    
    f = Math.round(x*100)/100;    
    return f;    
}
function timeFormat(ms){
    if (ms) {
        return new Date(ms).format("yyyy-MM-dd hh:mm:ss");
    }
    return "";
}
function timeFormatTime(ms){
    if (ms) {
        return new Date(ms).format("hh:mm:ss");
    }
    return "";
}
function timeInterval(creation_time,end_time){
    Date.prototype.diff = function(date){
        return (this.getTime() - date.getTime())/(1000*60);
    };
    if(!creation_time){ 
        return "";
    }
    var creatTime = new Date(creation_time);
    if(!end_time){ 
        return "";
    }
    var endTime = new Date(end_time);
    return toDecimal(endTime.diff(creatTime))+'m';
}
function redioFun(i){
    var value = $("#id"+i).parent().parent().attr("id");
    $("#tr"+select).removeClass();
    $("#id"+select).removeClass();
    $("#id"+select).addClass("icon icon-circle-thin");
    select =i;
    $("#"+value).addClass("selected");
    $("#id"+i).removeClass();
    $("#id"+i).addClass("icon icon-dot-circle");
    dockerDeployStats();
}
function componentsSize(str){
    var size=0;
    if (str) {
        var arr = str.split(",");
        arr = arrExSpace(arr);
        size = arr.length;
    }
    return size;
}
function arrExSpace(arr){
    if(arr) {
        for (var i=0;i<arr.length;i++){
            if (arr[i] == ""){
                arr.splice(i,1);
            }
        }
    }
    return arr;
}
function splitComponents(str){
    var component="";
    if (str) {
        var arr = str.split(",");
        arr = arrExSpace(arr);
        for ( var i in arr) {
            component+=arr[i]+"<br>";
        }
    }
    return component;
}
function firstComponents(str){
    var size=0;
    if (str) {
        var arr = str.split(",");
        arr = arrExSpace(arr);
        size = arr.length;
        if (size == 0) {
            return "";
        }else{
            return arr[0].substr(0,10)+" ("+size+")...";
        }
        
    }
    return "";
}
function splitImages(str){
    var images="";
    if(str){    
        var arr = str.split(" ");
        for ( var i in arr) {
            var imageArr = arr[i].split("/");
            if(imageArr.length > 1){
                images += imageArr[2]+"<br>";
            }else{
                images += imageArr[0]+"<br>";
            }
        }
    }
    return images;
}
function getFirstImage(str){
    var size=0;
    if(str){  
        var arr = str.split(" ");
        size = arr.length;
        if (size == 0) {
            return "";
        }else{
            var imageArr = arr[0].split("/");
            if(imageArr.length > 1){
                return imageArr[2].substr(0,10)+" ("+size+")...";
            }else{
                return imageArr[0].substr(0,10)+" ("+size+")...";
            }
            
        }
    }
    return "";
}
function booleanIcon(){
    $.each($("td.concurrent"), function (i, data) {
        if( data.textContent == 'true'){
            $(this).html('<i class="icon icon-check2"></i>');
        }else{
            $(this).html('<i class="icon icon-times"></i>');
        }
    });
    $.each($("td.aws"), function (i, data) {
        if( data.textContent == 'true'){
            $(this).html('<i class="icon icon-check2"></i>');
        }else{
            $(this).html('<i class="icon icon-times"></i>');
        }
    });
}
function globalStatus(){
    $.each($("td.globalstatus"), function (i, data) {
        if( data.textContent == 'true'){
            $(this).html('<i class="icon icongreen icon-smile"></i>');
        }else if (data.textContent == 'false') {
            $(this).html('<i class="icon iconred icon-frown"></i>');
        }else{
            $(this).html('<i class="icon icongrey icon-meh"></i>');
        }
    });
}
function iconStatus(){
    $.each($("td.iconstatus"), function (i, data) {
        if( data.textContent == 'true'){
            $(this).html('<i class="icon icongreen icon-smile"></i>');
        }else if (data.textContent == 'false'){
            $(this).html('<i class="icon iconred icon-frown"></i>');
        }else{
            $(this).html('<i class="icon icongrey icon-meh"></i>');
        }
    });
}
function rowFlag(el,key,flagValue){
    var key = key+"";
    var flagValue = flagValue +"";
    if (key.indexOf(flagValue)==-1?false:true) {
        el.setAttribute('class','flagBackground');
    }
    return key;
}
function dockerDeployStats(){
    var index = select;
    $("#process_info").html(''); 
    $("#deploy_status").html('');
    $("#deploy_status_panel").html('DEPLOY STATUS INFO');
    $("#log_info").html('');
    var dataLength=this.json.data.length;
    if (dataLength > 0){
        var stack_list = this.json.data[index].stack_list;
        
        var tar_complete_flag = this.json.data[index].tar_complete_flag;
        var tar_start_time = this.json.data[index].tar_start_time;
        var tar_end_time = this.json.data[index].tar_end_time;
        var image_complete_flag = this.json.data[index].image_complete_flag;
        var image_start_time = this.json.data[index].image_start_time;
        var image_end_time = this.json.data[index].image_end_time;
        var deploy_complete_flag = this.json.data[index].deploy_complete_flag;
        var deploy_start_time = this.json.data[index].deploy_start_time;
        var deploy_end_time = this.json.data[index].deploy_end_time;
        var application_name = this.json.data[index].application_name;
        
        var tarInterval = timeInterval(tar_start_time,tar_end_time);
        var imageInterval = timeInterval(image_start_time,image_end_time);
        var deployInterval = timeInterval(deploy_start_time,deploy_end_time);
        
        /*var stack_status = "";
        if(stack_list){
            stack_list = eval("(" + stack_list + ")");
            $.each(stack_list, function (i, data) {
                var stack_name = data.stack_name;
                if(stack_name != application_name){
                    return true;
                }
                stack_status = data.stack_status;
            });
        }*/
        
        //process info
        $("#process_info").html($("#process_info").html()
                +"<tr>"
                    +"<td class='iconstatus tarColor'>"+tar_complete_flag+"</td>"
                    +"<td class='tarColor'>"+timeFormatTime(tar_start_time)+"</td>"
                    +"<td class='tarColor'>"+tarInterval+"</td>"
                    +"<td class='iconstatus imageColor'>"+image_complete_flag+"</td>"
                    +"<td class='imageColor'>"+timeFormatTime(image_start_time)+"</td>"
                    +"<td class='imageColor'>"+imageInterval+"</td>"
                    +"<td class='iconstatus deployColor'>"+deploy_complete_flag+"</td>"
                    +"<td class='deployColor'>"+timeFormatTime(deploy_start_time)+"</td>"
                    +"<td class='deployColor'>"+deployInterval+"</td>"
                +"</tr>"
        );
        //deploy status info
        if (stack_list){
            stack_list = eval("(" + stack_list + ")");
            var components=stack_list.components;
            $("#deploy_status_panel").html("DEPLOY STATUS <span style='color:red;'>" + application_name + "</span> INFO");
            if (!components){
                return;
            }
            var n = -1;
            $.each(components, function (i, data) {
                var component_name = data.component_name;
                var component_status = data.status;
                var pods = data.pods;
                $.each(pods, function (j, data) {
                    var pods_status = data.status;
                    var pods_is_running = data.is_running;
                    var pods_is_ready = data.is_ready;
                    var pods_pod_IP = data.pod_IP;
                    var pods_host_IP = data.host_IP;
                    $("#deploy_status").html($("#deploy_status").html()
                            +"<tr>"
                                +"<td>"+component_name+"</td>"
                                +"<td>"+component_status+"</td>"
                                +"<td>"+pods_status+"</td>"
                                +"<td>"+pods_is_running+"</td>"
                                +"<td>"+pods_is_ready+"</td>"
                                +"<td>"+pods_pod_IP+"</td>"
                                +"<td>"+pods_host_IP+"</td>"
                            +"</tr>"
                    );
                    n += 1;
                    var el = document.getElementById('deploy_status').getElementsByTagName('tr')[n];
                    rowFlag(el.getElementsByTagName('td')[4],pods_is_ready,false);
                    rowFlag(el.getElementsByTagName('td')[1],component_status,'FAIL');
                });
            });
        }
        iconStatus();
        logInfo();
    }
}
function refresh(){
    getFbDB();
    $('.mytooltip').tooltip();
}
function autorefresh(){
    if (autoRefresh == true) {
        autoRefresh = false;
        $("#autorefresh").html(''); 
        $("#autorefresh").html($("#autorefresh").html()+"<i class=\"icon icon-check-empty\"></i>");
        clearInterval(refreshInt);
    }else{
        autoRefresh = true;
        $("#autorefresh").html(''); 
        $("#autorefresh").html($("#autorefresh").html()+"<i class=\"icon icon-check-sign\"></i>");
        refreshInt = setInterval(function(){
            getFbDB();
            $('.mytooltip').tooltip();
        }, 3000);
    }
}

function search(){
    select =0;
    currentPage = 1;
    creator = $("#creator").val();
    env = $("#env option:selected").val();
    getFbDB();
    $('.mytooltip').tooltip();
}
function pageCode(){
    var pageSum = Math.ceil(sumCount/pageCapacity);
    $(".tcdPageCode").createPage({
        pageCount:pageSum,
        current:currentPage,
        backFn:function(p){
            select =0;
            currentPage=p;
            getFbDB();
            $('.mytooltip').tooltip();
        }
    });
}
function logInfo(){
    var index = select;
    var id = this.json.data[index].id;
    var job_name = this.json.data[index].job_name;
    var build_id = this.json.data[index].build_id;
    var autoTest = this.json.data[index].autoTest;
    $("#log_info_panel").html("LOG INFO <span style='color:red;'>" + job_name +"  " +build_id+ "</span> INFO");
    $("#log_head").html("<a id=\"jenkinsLogHyperlink\" href=\"\" onclick=jenkinsLogHyperlink(\""+job_name+"\","+build_id+")>&nbsp;&nbsp;Jenkins log hyperlink</a>");
    $("#autoTest").html("<a id=\"autoTestHyperlink\" href=\"\" onclick=autoTestHyperlink(\"YM_WAJ_autoTest\","+autoTest+")>&nbsp;&nbsp;AutoTest Hyperlink</a>");
    if(logPanelOpen){
        logPanelOpen=false;
        logLoad();
    };
}
function logLoad(){
    if(!logPanelOpen){
        logPanelOpen=true;
    }else{
        logPanelOpen=false;
    }
    var id = this.json.data[select].id;
    $.ajax({
        type : "get",
        async:false,
        url : "ymDB/getLogInfo?id="+id,
        dataType : "html",
        beforeSend: LoadFunction,
        error: erryFunction,
        success: succFunction
    });
    function LoadFunction() {  
        $("#log_info").html('加载中...');  
    }
    function erryFunction() {  
       console.error("getLogInfo error");
    }
    function succFunction(res) {
        $("#log_info").html($("#log_info").html()
                +"<tr>"
                    +"<td><pre>"+res+"</pre></td>"
                +"</tr>"
        );
    }
}
function jenkinsLogHyperlink(job_name,build_id){
    window.open("http://172.30.10.138:8080/job/"+job_name+"/"+build_id+"/console");
}
function autoTestHyperlink(job_name,autoTest){
    window.open("http://172.30.10.138:8080/job/"+job_name+"/"+autoTest+"/testReport");
}


