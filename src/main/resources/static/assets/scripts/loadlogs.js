 var deftheme;
    var resultsPath;
    let clipBoard1=new ClipboardJS('#copyresultspath');
		$(document).ready(function(){
			$("#successmsg").hide();
             deftheme=true;
           $("#resultpathvar").val(localStorage.resultspath);
           $("#buildno").html(localStorage.buildno);
            fetchData();
			 $("#myInput").on("keyup", function() {
				    var value = $(this).val().toLowerCase();
				    $("#containbody *").filter(function() {
				      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
				    });
                  });
                  $("#copyresultspath").click(function(){
                    console.log($("#resultpathvar").val())
                    $('.toast').toast({delay: 2000});
                    $('.toast').toast('show');
              }); 
        });

		$("#viewreport").click(function(){
			window.open("/viewreport.html","_blank");
		});
		
		   $("#sendresults").click(function(){
	            $(".spinner-border").show();
	            var reqbody={
                    fromAddress:"apiexecutor@gmail.com",
                    toAddresses:$("#sendresemail").val(),
                    subject:$("#sendressub").val(),
                    body:$("#sendresbody").val(),
                    execResultsPath:$("#resultpathvar").val()
                }
	            $.ajax({
	                type:"POST",
	                headers: {"Content-Type":"application/json"},
	                url:"/apiexe/sendMail",
	                data:JSON.stringify(reqbody),
	                success: function(result){
	                    $(".spinner-border").hide();
	                },error: function(result){
	                    $(".spinner-border").show();
	                    alert(error);
	                }
	            });
	        })
		
		$("#downloadlog").click(function(){
			$.ajax({
				type:"GET",
				url:"/apiexe/downloadFile",
				success : function(result){
					console.log("Success");
					filedownload(result);
				},
				error : function(result){
					console.log("error")
				}
			});
		});
       
        $("#switch").click(function(){
            deftheme=!deftheme;
            if(deftheme){
                $("#containbody").css('background-color','black');
                $("#logresult").css('color','white');
                $("#switch").css('background-color','black');
                $("#switch").css('color','white');
            }
            else{
                $("#containbody").css('background-color','white');
                $("#logresult").css('color','black');
                $("#switch").css('background-color','white');
                $("#switch").css('color','black');
            }
        });
		function fetchData(){
			var timeout;
			$.ajax({
				type : "GET",
				url : "/apiexe/readAPILog",
				success : function(result) {
					console.log(result);
					document.getElementById("logresult").innerHTML=" ";
					document.getElementById("logresult").innerHTML=result.logRecords.join(' ');
					if(result.passcases==null || result.passcases==""){
						result.passcases=0;
					}
					if(result.failcases==null || result.failcases==""){
						result.failcases=0;
					}
					var succperc=result.passcases.toString().concat("%");
					var failperc=result.failcases.toString().concat("%");
					$("#successprog").css('width',succperc);
					$("#failprog").css('width',failperc);
					$("#successprog").html(succperc);
					$("#failprog").html(failperc);
					if(false){
						clearTimeout(timeout);
					}else{
						console.log("Loopinggg")
					result=" ";
					timeout=setTimeout(fetchData,1000);
					}
				},
				error : function(result) {
					alert('error');
				}
			});
			}
		
		function filedownload(result){
			var blob=new Blob([result],{type:"text/plain;charset=utf-8"});
			saveAs(blob,"log.log")
		}