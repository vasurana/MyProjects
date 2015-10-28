$(document).ready(function() {
    $("#submitBtn").click(function() {
            $("#firForm").addClass("hidden");
            $("#done").removeClass("hidden");
            $("#submitBtn").addClass("hidden");
            $("#firForm")[0].reset()
    });
    $("#submitFIRBtn").click(function() {
    	$("#firForm").removeClass('hidden');
        $("#submitBtn").removeClass("hidden");
        $("#done").addClass("hidden");
    });
});