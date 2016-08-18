/**
 * 调用后台批量删除方法
 * 
 */
function deleteBatch(basePath){
	$("#mainForm").attr("action",basePath+"DeleteBatchServlet");
	$("#mainForm").submit();
}
function myFunction()
{
    alert("你好，我是一个警告框！");
}