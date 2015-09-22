function remove_span_font_family() {
    var spanEl = document.getElementsByTagName("span");
    for (var i=0; i<spanEl.length; i++) {
        spanEl[i].setAttribute("style", "color:#000000;font-size:18px");
    }
}

function add_img_tag_width() {
    var imgEl = document.getElementsByTagName("img");
    for (var i=0; i<imgEl.length; i++) {
        imgEl[i].setAttribute("width", "100%");
    }
}
