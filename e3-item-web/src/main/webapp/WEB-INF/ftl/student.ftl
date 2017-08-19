<html>
<head>
<meta http-equiv=Content-Type content="text/html;charset=utf-8">
</head>
<body>
学生详情:<br>
	id:${student.id}<br>
	姓名:${student.name}<br>
	年龄:${student.age}<br>
	地址:${student.address}
	<table border="1">
		<tr>
			<td>序号</td>
			<td>id</td>
			<td>姓名</td>
			<td>年龄</td>
			<td>地址</td>
		</tr>
		<#list stuList as stu>
		<#if stu_index%2==0>
		<tr bgcolor="red">
		<#else>
		<tr bgcolor="yellow">
		</#if>
			<td>${stu_index}</td>
			<td>${stu.id}</td>
			<td>${stu.name}</td>
			<td>${stu.age}</td>
			<td>${stu.address}</td>
		</tr>
		</#list>
	</table>
		<br>
		当前日期:${date?date}<br>
		当前日期:${date?time}<br>
		当前日期:${date?datetime}<br>
		当前日期:${date?string("yyyy/MM/dd HH/mm/ss")}<br>
		null:${val!"laiya"}<br>
		null:
		<#if val??>
			val的值:${val}
		<#else>
			val的值为null
		</#if>>
</body>
</html>
