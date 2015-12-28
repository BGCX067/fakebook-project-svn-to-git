<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title></title>
		<link type="text/css" rel="stylesheet" href="<c:url value="/css/infovis.css"/>" />
		<link type="text/css" rel="stylesheet" href="<c:url value="/css/style.css"/>" />
		<link type="text/css" rel="stylesheet" href="<c:url value="/css/jquery-ui-1.7.1.css"/>" />
		<link type="text/css" rel="stylesheet" href="<c:url value="/css/ui.slider.extras.css"/>" />
		<script type="text/javascript" src="<c:url value="/js/mootools-1.2.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/jquery-1.3.2.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.7.1.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/selectToUISlider.jQuery.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/Hypertree.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/infovis.js"/>"></script>
		
		<style type="text/css">
			.ui-slider {clear: both;margin-left: 20px;margin-right: 20px};
		</style>
		
		<script language="javascript">
			var ZOOM_MODE = true;
			var NORMAL_CLICK = true;
			var $j = jQuery.noConflict();
			$j(document).ready(function(){
				Infovis.initLayout();
				Config.drawMainCircle = true;
				Config.allowVariableNodeDiameters = true;
				Config.animationTime = 800;
				Config.transformNodes = false;
				
				var canvas= new Canvas('infovis', '#fff', '#000');
				var con = {
				
				  	//開始計算
				  	onBeforeCompute: function(node) {
						Log.write("centering...");
 					},
 														
				  	//建立 node label
				  	onCreateLabel: function(domElement, node) {
				  		var d = $(domElement);
				  		var dj = $j(domElement);
						d.set('tween', { duration: 300 }).setOpacity(1).set('html', node.name).addEvents({
						
							orig : "",
							
							'click': function(e) {
								if(NORMAL_CLICK) {
									ht.onClick(e.event);
								}
								NORMAL_CLICK = true;
							},
		
							//when mouse over trigger this function
							'mouseenter': function() {
								var b = 'solid 1px red';
								d.style.cursor = 'pointer';
								d.tween('opacity', 1);
								d.style.border = b;
								orig = dj.html();
								dj.css('background-color', '#f8f8f8').css('z-index', '99');
								dj.append("<span>&nbsp;(" + node.data[0].gender + ")</span>");
								dj.append("<div>" + node.data[0].groupLinks+ "</div>");
								dj.find("a").click(function(e){
		 							var detail = $j("<div></div>");
		 							detail.append("<img width='150px' height='150px' src='<c:url value="/group/photo.do?id=" />" + $j(this).attr('groupId') + "'>");
		 							detail.append("<div style='margin-top:15px'>" + this.text + "</div>");
			 						$j('#inner-details').html(detail.html());
									var con = $_.merge(ht.controller, getController("/"+this.text+"/ig.test(n.data[0].groupLinks)"));
									GraphPlot.plot(ht,con); 
									ht.controller = con;				  	
									NORMAL_CLICK = false;
								});
								dj.find('img').width(50).height(50).show();
							},
							
							// when mouse out trigger this function
							'mouseleave': function() {
								var nb = '0px';
								d.tween('opacity', 1);
								d.style.border = nb;
								dj.html(orig);
								dj.css('background-color', 'transparent').css('z-index', '1');
								dj.find('img').hide();
							}
						});
						var img = $j(domElement).find("img")
						img.hide().attr("origWidth", img.width()).attr("origHeight", img.height());
						node.jquery = $j(domElement);
					},
	  	
				  	// when comput finished trigger this function
				  	onAfterCompute: function() {
				  		var node = GraphUtil.getClosestNodeToOrigin(ht.graph, "pos");
 						var detail = $j("<div></div>");
 						detail.append(node.name);
 						detail.find('img').width(140).height(140);
 						detail.append("<div>&nbsp;(" + node.data[0].gender + ")</div>");
 						$j('#inner-details').html(detail.html());
  						if(ZOOM_MODE){
  							var url = "<c:url value="/person/person.do?pid=" />" + node.id.replace('node', '');
				  			ht.morph(url, node);
				  		}
				  		Log.write("done");
				  	}
				};
				  	
				// create hyperbolic tree
				var ht= new Hypertree(canvas, con);
				ht.controller = $_.merge(ht.controller, getController("true")); 
				
				// get data from server using json
				$j.getJSON("<c:url value="/person/person.do?pid=${param.pid}" />" , function(json){
 					ht.loadGraphFromJSON(json,3);
					ht.refresh();
				});
				
				// when filter button click trigger this function
				$j('#filter input[type="button"]').click(function() {
					filter();					
				});
				
				// switch zoom mode
				$j('#zoom').click(function() {
					ZOOM_MODE = (this.checked) ? true : false;
				});
				
				// when gender select changed
				$j("#gender").change(function() {
					filter();
				});
						
				// Sociality slider bar
				$j("#friends").selectToUISlider({
					labelSrc: 'text',
					labels: 2,
					tooltip: false,
					sliderOptions : {
						stop: function(event, ui) {
							filter();
						}
					}
				});
				
				// Activity slider bar
				$j('#dayAgo').selectToUISlider({
					labels: 2,
					labelSrc: 'text',
					tooltip: false,
					sliderOptions : {
						stop : function(){
							filter();
						}
					}
				});
				
				// when click global button trigger this function
				$j('#global').click(function() {
					location.href = "<c:url value="/person/visualize.do" />"
				});
				
				// when click local button trigger this function
				$j('#local').click(function() {
					location.href = "<c:url value="/person/visualize.do?pid=26" />"
				});
				
				// switch "AND" "OR"
				$j('input[name="condition"]').click(function() {
					filter();
				});
				
								
				// switch post info or gender info
				$j('#line').change(function() {
					$j('#genderInfo, #postInfo').toggle();
					filter();
				});
				
				// highlight node if match keyword
				function filter() {
					var condition = $j('input[name="condition"]:checked').val();
					var text = $j('#filter input[type="text"]').val();
					var val = $j('#gender').val(); 
					var con = '';
					if(text != '') {
						var condi = '';
						var filter = text.split(',');
						for(var i=0;i < filter.length;i++) {
							if(i == filter.length -1) {
								condi += "/"+$j.trim(filter[i])+"/ig.test(n.name)";								
							} else {
								condi += "/"+$j.trim(filter[i])+"/ig.test(n.name) || ";
							}						
						}
						con += "(" + condi + ")";
						con += condition;
					} 
					if( val == 'Female' || val == 'Male') { 
						con += "(n.data[0].gender == $j('#gender').val())";
					} else if(val == 'Hetero' || val == 'Homo') {
						con += "(" + val + ")";
					} else if(val == 'All') {
						con += "true";
					}
					con += condition;
					con += "(n.data[0].friends > $j('#friends').val())";
					con += condition;
					con += "(n.data[0]." + $j('#dayAgo').val() + " == 'true')"; 
					con = $_.merge(ht.controller, getController(con));
					GraphPlot.plot(ht,con); 				  	
					ht.controller = con;
					
				}
				
				// hyperbolic tree controller
				function getController(option) {
					 return {
					 	lineW : canvas.getContext().lineWidth, 
					 	lineC: canvas.getContext().strokeStyle,
					 	nodeF: canvas.getContext().fillStyle,
					 	alpha : 1,
						onBeforePlotLine: function(adj) {
							//canvas.getContext().lineWidth = adj.data.weight;
							canvas.getContext().lineWidth = 1.5;
							var n = adj.nodeFrom; var t = adj.nodeTo;
							var Hetero = t.data[0].gender != n.data[0].gender;
							var Homo = t.data[0].gender == n.data[0].gender;
							if(eval(option)) {
								if($j('#line').val() == 'post') {
									canvas.getContext().strokeStyle = getLineColor(adj.data.weight);
								} else {
									canvas.getContext().strokeStyle = adj.data.color;
								}
							} else {
								adj.alpha = 0.3;
								canvas.getContext().strokeStyle = '#ccc';
								n = t
								if(eval(option)) {
									adj.alpha = this.alpha;
									if($j('#line').val() == 'post') {
										canvas.getContext().strokeStyle = getLineColor(adj.data.weight);
									} else {
										canvas.getContext().strokeStyle = adj.data.color;
									}
								}
							}
					  	},
					  	onAfterPlotLine: function(adj) {
							adj.alpha = this.alpha;
					  		canvas.getContext().lineWidth = this.lineW;
							canvas.getContext().strokeStyle = this.lineC;
					  	},
					  	onBeforePlotNode: function(node) {
					  		var n = node; this.alphaN = n.alpha; var Hetero = true;	var Homo = true;
					  		if(eval(option)) {
								if(n.jquery) {
									n.jquery.find('span').css('color', 'black');
								}
								canvas.getContext().fillStyle = getColor(n);
							} else {
								if(!ZOOM_MODE) {
									n.alpha = 0.3;
								}
								if(n.jquery) {
									n.jquery.find('span').css('color', '#ccc');
								}
								canvas.getContext().fillStyle = '#ccc';
							}
					  	},
					  	onAfterPlotNode: function(node) {
					  		if(!ZOOM_MODE) { 
					  			node.alpha = this.alpha;
					  			canvas.getContext().fillStyle = node.nodeF;
					  		}
					  	}
					};
				}
			
				// switch two dataset animotion
				Hypertree.prototype.morph = function(url, node) {
					var ht = this;
					$j.getJSON(url , function(json){
			  			Log.write("morphing...");
				  		GraphOp.morph(ht, json, {
				  			'id' : node.id,
		  					'type': 'fade:seq',
		  					'duration':800,
							onComplete: function() {
								Log.write("done");					
							},
							onAfterCompute: $lambda(),
		  					onBeforeCompute: $lambda()
		  				});
					});
				};
				
			});
			
			// get gender color
			function getColor(node) {
				return (node.data[0].gender == 'Male') ? 'blue' : 'red';
			}
			
			// get post info color map
			function getLineColor(weight) {
				return COLORMAP[weight];
			}
			
			var COLORMAP = {
				"1" : "#2D7A9C",
				"2" : "#E68477",
				"3" : "#9C612D",
				"4" : "#7B3E80",
				"5" : "#00631C"
			}
		</script>
	</head>

	<body>
		<div id="header"></div>
		<div id="left">
			<div  class="element contained-item">
				<form id="filter" onsubmit="return false">
					<input type="text" size="10" style="margin-left: 20px;margin-top: 30px;margin-bottom: 20px"/>
					<input type="button" value="Search" />
				</form>
				<input type="checkbox" id="zoom" style="margin-left: 20px;margin-bottom: 30px" checked/><span>Zoom Mode</span>
				<input type="radio" name="condition" style="margin-left: 20px" checked value="&&"/> AND &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
				<input type="radio" name="condition" value="||"/> OR
				<fieldset>					
					<label style="margin-left: 10px">Gender : </label>
					<select id="gender" >
						<option value="All" selected>All</option>
						<option value="Male">Male</option>
						<option value="Female">Female</option>
						<option value="Hetero">Hetero</option>
						<option value="Homo">Homo</option>
					</select>
					<label style="margin-left: 40px">Line : </label>
					<select id="line" style="width: 120px">
						<option value="gender" selected>Gender</option>
						<option value="post">Post Messages</option>
					</select>
				</fieldset>
				<div id="genderInfo">
					<table border="0" style="font-size: 11px;margin-left: 20px">
						<tr>
							<td bgcolor="#f6af3a" width="50px"></td>
							<td width="50px">Hetero</td>
							<td bgcolor="#4094c4" width="50px"></td>
							<td width="50px">Homo</td>
						</tr>
					</table>
				</div>
				<div id="postInfo" style="display:none">
					<table border="0" style="font-size: 11px;margin-left: 20px">
						<tr align="center">
							<td width="40px" bgcolor="#2D7A9C">1</td>
							<td width="40px" bgcolor="#E68477">2</td>
							<td width="40px" bgcolor="#9C612D">3</td>
							<td width="40px" bgcolor="#7B3E80">4</td>
							<td width="40px" bgcolor="#00631C">5</td>
						</tr>
					</table>
				</div>
				<br>
				<fieldset>
					<label>Sociality : </label>
					<select id="friends" style="display:none">
						<option value="1" selected>1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">10</option>
						<option value="10">10</option>
					</select>
				</fieldset>
				<br>
				<fieldset>
					<labe>Activity : </label>
					<select id="dayAgo" style="display:none">
						<option value="month1" selected>30 days</option>
						<option value="week1">1 Week</option>
						<option value="day5">5 Days</option>
						<option value="day3">3 Days</option>
						<option value="today">0 day</option>
					</select>
				</fieldset>
			</div>
			<br>
			<br>
			<br>
			<input type="button" id="global" value="Global" style="margin-left: 40px;margin-right: 20px"/>
			<input type="button" id="local" value="Local"/>
			<br>
			<br>
			<br>
			<br>
			<div class="element contained-item">
				<div class="inner" id="inner-details" align="center"></div>
			</div>
		</div>
		<canvas id="infovis"></canvas>
		<div id="label_container"></div>
		<div id="log" style="margin-right:20px;float:right;display:none"></div>
	</body>
	
</html>

