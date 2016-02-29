function add_img_tag_width() {
    var imgEl = document.getElementsByTagName("img");
    for (var i=0; i<imgEl.length; i++) {
        imgEl[i].setAttribute("width", "100%");
        imgEl[i].removeAttribute("height");
    }
}