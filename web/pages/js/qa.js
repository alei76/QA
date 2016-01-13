/**
 * Created by azurexsyl on 2015/7/7.
 */

$(
    function() {
        $("#search").bind("click", search);
    }
)

function search() {
    var question = $("#question").val();
    console.log(question);
    $.ajax({
        dataType: "json",
        url: "/ask",
        data: {"question" : question,
                "category" : "rdf"},
        success: getAnswersSCB
    });
}

function getAnswersSCB(result, textStatus, jqXHR) {
    /*console.log(result);
    var content = "";
    $.each(result.results, function(cnt, data){
            content += "<div class='ui items'><div class='item'>";
        content += "<div class='ui small image'><img src='/resources/images/image.png' /></div>";
            content += "<div class='middle aligned content'><div class='header'>答案</div>";
            content += "<div class='ui celled list'>";
            $.each(data.objects, function(incnt, indata){
                content += "<div class='item'><div class='content'>" + indata + "</div></div>";
                /!*content += "<p>" + indata + "</p>";*!/
            });
            content += "</div>";
            content += "</div></div></div>";
        }
    );*/
    $("#answer").html(result.result);
}
