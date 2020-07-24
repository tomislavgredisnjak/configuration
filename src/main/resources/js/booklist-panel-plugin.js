var exerciseApp = exerciseApp || {};

var itemsPerPage;
var BreakException = {};
var page = 0;
var current_page = 1;
var enablePaging;
var numPages;
var missing = 0;
var title;
var author;
exerciseApp.getBooks = function (cb) {

    AJS.$.ajax({
        url: AJS.contextPath() + "/rest/exercise/1.0/book",
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
            cb(data);
        }
    });
}



exerciseApp.setTable = function () {

    AJS.$.ajax({
        url: AJS.contextPath() + "/secure/admin/ConfigurationWebworkAction!default.jspa",
        type: "GET",
        success: function (data) {
            var parser = new DOMParser();
            var htmlData = parser.parseFromString(data, 'text/html');
            itemsPerPage = htmlData.getElementById("items-per-page").value;
            enablePaging = htmlData.getElementById("enablePaging").value;
            if (enablePaging === 'on') {
                exerciseApp.getBooks(function (data) {
                    numPages = Math.ceil(data.books.length / itemsPerPage);
                    var i;
                    for (i = 0; i < itemsPerPage; i++) {
                        var html = ExerciseApp.SoyTemplates.bookItemRow(data.books[i]);
                        AJS.$("#booklistHandle").find("tbody").append(html);
                    }
                });
            } else {
                exerciseApp.getBooks(function (data) {
                    data.books.forEach(function (bookItem) {
                        var html = ExerciseApp.SoyTemplates.bookItemRow(bookItem);
                        AJS.$("#booklistHandle").find("tbody").append(html);
                    });
                });
            }
        }
    });
}



function save() {
    title = document.getElementById("title").value;
    author = document.getElementById("author").value;
    console.log(title);
    $.ajax({
        url: AJS.contextPath() + "/rest/exercise/1.0/book?title=" + title + "&author=" + author,
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        success: function () {
            console.log("Added book: " + title + " by " + author);
        }
    });
}

function deleteBook(id) {
    console.log(id);
    $.ajax({
        url: AJS.contextPath() + "/rest/exercise/1.0/book/" + id,
        type: "DELETE",
        dataType: "json",
        contentType: "application/json",
        success: function () {
            console.log("Deleted book with id: " + id);
            alert("Refresh page to see remaining books.");
        }
    });
}

AJS.toInit(function () {
    console.log("Booklist Panel JS loaded!");
    exerciseApp.setTable();

});

function prevPage() {
    if (current_page > 1 && missing < 1) {
        missing = 0;
        current_page--;
        $("table tr").slice(-itemsPerPage).remove();

        exerciseApp.getBooks(function (data) {
            var i;
            for (i = itemsPerPage * (current_page - 1); i < itemsPerPage * current_page; i++) {
                if (i < data.books.length) {
                    var html = ExerciseApp.SoyTemplates.bookItemRow(data.books[i]);
                    AJS.$("#booklistHandle").find("tbody").append(html);
                }
            }
        });
    } else {
        if (current_page > 1) {
            current_page--;
            $("table tr").slice(-(itemsPerPage - missing)).remove();
            missing = 0;
            exerciseApp.getBooks(function (data) {
                var i;
                for (i = itemsPerPage * (current_page - 1); i < itemsPerPage * current_page; i++) {
                    if (i < data.books.length) {
                        var html = ExerciseApp.SoyTemplates.bookItemRow(data.books[i]);
                        AJS.$("#booklistHandle").find("tbody").append(html);
                    }
                }
            });
        }
    }
}

function nextPage() {
    current_page++;
    if (current_page <= numPages) {

        $("table tr").slice(-itemsPerPage).remove();
        exerciseApp.getBooks(function (data) {
            var i;
            for (i = itemsPerPage * (current_page - 1); i < itemsPerPage * current_page; i++) {
                if (i < data.books.length) {
                    var html = ExerciseApp.SoyTemplates.bookItemRow(data.books[i]);
                    AJS.$("#booklistHandle").find("tbody").append(html);
                } else {
                    missing++;
                }
            }
        });
    } else {
        current_page--;
    }
}