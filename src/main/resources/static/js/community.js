/**提交回复**/
function post() {
    var question_id = $("#question_id").val();
    var comment_content = $("#comment_content").val();
    comment2target(question_id, 1, comment_content);

}

/*二级评论功能 要获得输入里的content 以及*/
function comment(e) {
    var id = e.getAttribute("data-id");
    var comment = $("#input-" + id).val();
    comment2target(id, 2, comment);

}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("亲，不能回复空内容！");
        return;
    }
    console.log(question_id);
    console.log(comment_content)
    $.ajax({
        type: "post",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type

        }),
        success: function (response) {

            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=2a5e20191d2396effccc&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closable", true);//把数据存在页面中
                    }
                }
                alert(response.message);
            }
        },
        dataType: "json",
    });
}

/**
 * 展开二级评论**/
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    /**如果有class In就移除，没有就添加。*/
    if (comments.hasClass("in")) {
        comments.removeClass("in");
        e.classList.remove("active")
    } else {

        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            comments.addClass("in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    /*我对这个div理解为C=一个div。如果是ul 就是C=一个Ul*/
                    var mediaLeftElement=$("<div/>",{
                       "class":"media-left"
                    }).append($("<img/>",{
                        "class":"media-object rounded",
                        "src":comment.user.avatarUrl,
                        "alt":"..."
                    }));
                    var mediaBodyElement=$("<div/>",{
                        "class":"media-body",
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        html:comment.user.name,
                    })).append($("<div/>",{
                        html:comment.content,
                    })).append("<div/>",{
                        "class":"menu"
                    }).append($("<span/>",{
                        "class":"pull-right",
                        html:moment(comment.gmtCreate).format('YYYY-MM-DD')
                    }));
                    var mediaElement=$("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(mediaElement);
                });
                comments.addClass("in");
                e.classList.add("active");
            });

        }


    }
}



function selectTag(value) {
    var tagContent=$("#tag").val();
    if(value!=null&&tagContent.indexOf(value)==-1) {
        if (tagContent) {
            $("#tag").val(tagContent + "," + value);
        } else {
            $("#tag").val(value);
        }
    }
}