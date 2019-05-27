<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://fonts.googleapis.com/css?family=Noto+Serif+TC"
	rel="stylesheet">
<link rel="stylesheet" href="Styles/midpro2.css">
<title>Form</title>
</head>
<body>
	<form method="POST" action="dataProcess" class="cForm">
		<div class="titleText">台北市冷暖</div>
		<div>
			<label class="inputdata">地區<select name="location"
				id="locationList"></select></label>
			<h3>${errorMsg.location}</h3>
		</div>
		<div>
			<label class="inputdata">日期<input id="date" type="text"
				name="date" value="${param.date}" /></label><span>(yyyy/MM/dd)</span>
			<h3 id="dateError">${errorMsg.date}</h3>
		</div>
		<div>
			<label class="inputdata">時間<select id="time" name="time">
					<option value="00:00:00">00:00:00</option>
					<option value="03:00:00">03:00:00</option>
					<option value="06:00:00">06:00:00</option>
					<option value="09:00:00">09:00:00</option>
					<option value="12:00:00">12:00:00</option>
					<option value="15:00:00">15:00:00</option>
					<option value="18:00:00">18:00:00</option>
					<option value="21:00:00">21:00:00</option>
			</select></label>
			<h3>${errorMsg.time}</h3>
		</div>
		<div>
			<label class="inputdata">溫度<input id="temperature"
				type="text" name="temperature" value="${param.temperature}" />
			</label>
			<h3>${errorMsg.temperature}</h3>
		</div>
		<input type="hidden" id="action" name="action" />
		<div class="button">
			<input type="submit" value="新增" name="insert" onclick="insertData()" />
			<input type="submit" value="修改 " name="update" onclick="updateData()" />
			<input type="submit" value="刪除" name="delete"
				onclick="deleteByLocationNDatetime()" /> <input type="button"
				value="查詢" name="findOne" onclick="selectOne()" /> <input
				type="button" value="查詢地區" name="findByLocation"
				onclick="selectByLocation()" /> <input type="button" value="查詢全部"
				name="findAll" onclick="selectAll()" />
		</div>
	</form>
	<hr>
	<div id="show" style="text-align: center;"></div>
	<div id="pageButton" style="text-align: center;"></div>
	<h3>${errorMsg.existData}</h3>
	<div style="text-align: center;" id="piclocation">
		<img src='images/222.jpg' class='pic1' id='p1'>
	</div>
	<script>
		let onePage = 10;
		let degree;
		let datas;
		var pageNow;
		var xhttp = new XMLHttpRequest();
		xhttp.onload = function() {
			document.getElementById("locationList").innerHTML = xhttp.responseText;
		}

		xhttp.open("POST", "generateList", true)
		xhttp.send();

		var xhttp2 = new XMLHttpRequest();
		xhttp2.onload = function() {
			var doc = JSON.parse(xhttp2.responseText);
			var txt2 = "";
			txt2 += doc[0].locationName;
			txt2 += doc[0].geocode;
			txt2 += " 緯度" + doc[0].lat;
			txt2 += " 經度" + doc[0].lon;
			txt2 += doc[0].element;
			txt2 += doc[0].description;
			txt2 += "<b>" + doc[0].dateTime + "</b>";
			txt2 += "<h3>" + doc[0].value + "</h3>";
			txt2 += doc[0].measures;
			txt2 += "<br>";

			degree = doc[0].value;
			document.getElementById("show").innerHTML = txt2;
			document.getElementById("pageButton").innerHTML = "";
			if (degree > 25) {
				showPicture();
			}
		}

		let pause = 2000;
		function change1() {
			document.getElementById("p1").classList.add("p1");
		}
		function change2() {
			document.getElementById("p1").classList.add("p2");
		}
		function change22() {
			document.getElementById("p1").classList.remove("p2");
		}
		function showPicture() {
			setTimeout(function() {
				change2();
				setTimeout(change22, 1500);
			}, 500);
		}
		change1();

		var xhttp3 = new XMLHttpRequest();
		function insertData() {
			document.getElementById("action").value = "InsertData";
		}
		function updateData() {
			document.getElementById("action").value = "UpdateData";
		}
		function deleteByLocationNDatetime() {
			document.getElementById("action").value = "DeleteData";
		}
		function selectOne() {
			xhttp2.open("POST", "dataProcess", true);
			var location = document.getElementById("locationList").value;
			var date = document.getElementById("date").value;
			var time = document.getElementById("time").value;
			var queryString = "location=" + location + "&date=" + date
					+ "&time=" + time + "&action=FindOne"
			xhttp2.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded");
			xhttp2.send(queryString);
		}
		var xhttp4 = new XMLHttpRequest();

		xhttp4.onload = function() {
			pageNow = 1
			datas = JSON.parse(xhttp4.responseText);
			var dataCount = datas.length;
			var pageTotal;
			if (dataCount % onePage == 0) {
				pageTotal = parseInt(dataCount / onePage);
			} else {
				pageTotal = parseInt(dataCount / onePage) + 1;
			}

			var txt5 = "";

			for (j = 1; j <= pageTotal; j++) {
				txt5 += "<input type='button' value='" + j
						+ "' onclick='changePage(" + j + ")'/>";
			}

			document.getElementById("pageButton").innerHTML = txt5;
			changePage(pageNow);
		}

		function changePage(num) {
			pageNow = parseInt(num);
			var txtk = "";
			for (k = (pageNow - 1) * onePage; k < pageNow * onePage; k++) {
				if (k == datas.length) {
					break;
				}
				txtk += datas[k].locationName;
				txtk += datas[k].geocode;
				txtk += " 緯度" + datas[k].lat;
				txtk += " 經度" + datas[k].lon;
				txtk += datas[k].element;
				txtk += datas[k].description;
				txtk += "<b>" + datas[k].dateTime + "</b>";
				txtk += "<h3>" + datas[k].value + "</h3>";
				txtk += datas[k].measures;
				txtk += "<br>";
			}
			document.getElementById("show").innerHTML = txtk;

		}

		function selectAll() {

			xhttp4.open("POST", "dataProcess", true);
			xhttp4.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded");
			xhttp4.send("action=FindAll");
		}
		function selectByLocation() {
			xhttp4.open("POST", "dataProcess", true);
			var location = document.getElementById("locationList").value;

			var queryString = "location=" + location + "&action=FindByLocation"
			xhttp4.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded");
			xhttp4.send(queryString);

		}
		var tempError = document.getElementById("dateError");
		var dateEle = document.getElementById("date");
		dateEle.addEventListener("blur", function() {
			if (dateEle.value == "" || dateEle.value == null) {
				tempError.innerHTML = "不可空白";
			}else{
				tempError.innerHTML = "";
			}

		});
	</script>
</body>
</html>