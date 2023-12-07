$(document).ready(function(){
			$("#build").hide();
			$("#envs").hide();
			$("#suites").hide();
			$("#run").hide();
			$("#resultsfolder").hide();
			$("#datafolder").hide();
			$(".spinner-border").hide();
			$("#exefilemandatory").hide();
			$("#resultsfoldermandatory").hide();
			$("#buildnomandatory").hide();
		});
		
		$("#apisbmt").click(function(){
			var apifiledata = $("#apifile").val();
			if(apifiledata=='' || apifiledata==null){
				$("#exefilemandatory").show();
			}else{
				$("#exefilemandatory").hide()	;
			$(".spinner-border").show();
			$.ajax({
				type:"GET",
				headers: {"Content-Type":"application/json"},
				url:"/apiexe/readRunnerParams",
				data:{
					filelocation:encodeURI($("#apifile").val())
				},
				success : function(result) {
					//alert('API Execution is completed..');
					$("#apisbmit").hide();
					$(".spinner-border").hide();
					showRunnerValues(result);
				},
				error : function(result) {
					alert('error');
				}
			});
		}	
	})

		var environments;
		function showRunnerValues(result){
			$("#build").show();
			$("#envs").show();
			$("#suites").show();
			$("#run").show();
			$("#resultsfolder").show();
			$("#datafolder").show();
			var dropdown=$("#envdropdown");
			dropdown.empty();
			environments=result.runnerparams.ENVDETAILS;
			var suites=result.runnerparams.SUITESDETAILS;
			$.each(environments,function(key,entry){
				console.log(entry);
				console.log(key);
				dropdown.append($('<a class="dropdown-item"></a>').attr('value',entry.name).text(entry.code).click(function(){
					$(this).closest(".dropdown").find("button").html($(this).text())
				})
				)
			})
			
			var suiteslist=$("#testsuite");
			$.each(suites,function(key,entry){
				suiteslist.append($('<option></option>').attr('value',entry.name).text(entry.code))
			})
			$('#testsuite').multiselect();
			$(".multiselect-selected-text").html("Select Suite")
		}

		$("#tdfolderpath").change(function(e){
			alert(e);
			e=JSON.Stringify(e);
			alert(e);
			const files=e.target.files;
			alert(files);
			console.log(e);
  			alert("The text has been changed.");
		});

		$(".custom-file-input").on("change", function() {
  		var fileName = $(this).val().split("\\").pop();
  		$(this).siblings(".custom-file-label").addClass("selected").html(fileName);
		});

		 function returnenv(envs){
			return new Promise(function(resolve,reject){ 
			console.log("Entered promise")
  			$.each(environments,function(key,entry){
      		if(entry.code===envs){
          		console.log(entry.name)
          		resolve(entry.name);
      				}
  				})
   			});
		}
		 $("#sbmt").click(async function(e) {
			 var res=$("#resultsfolder").val()
			 var build=$("#buildno").val()
			 var resmandatory=false;
			 var builnomandatory=false;
				if(res=='' || res==null){
					$("#resultsfoldermandatory").show()
				}else{
					resmandatory=true;
				}
				if(build==''||build==null){
					$("#buildnomandatory").show()
				}else{
					builnomandatory=true;
				}
					
			if(resmandatory && buildnomandatory) {
				$("#resultsfoldermandatory").hide()
				$("#buildnomandatory").hide()
			localStorage.resultspath=$("#resultsfolder").val();
			localStorage.buildno=$("#buildno").val();
			e.preventDefault();
			console.log($("#buildno").val());
			console.log($("#testsuite").val());
			var envvalue=await returnenv($("#envbtn").text());
			console.log(envvalue)
			var reqbody={
				testresultsfolder:$("#resultsfolder").val(),
				testdatafolder:$("#datafolder").val(),
				apiexefile:$("#apifile").val(),
				buildnumber:$("#buildno").val(),
				environment:envvalue,
				suites:$("#testsuite").val()
			}
			window.open("/loadlogs.html","_blank");
			$.ajax({
				type : "POST",
				headers: {"Content-Type":"application/json"},
				url : "/apiexe/runAPI",
				data : JSON.stringify(reqbody),
				success : function(result) {
					alert('API Execution is completed..');
				},
				error : function(result) {
					alert('error');
				}
			});
		 }
		});