function backRawData() {
    var id=document.getElementById("rawTable");
    //alert("test1");
    var text=rawdata.fn.renderMain();
    //alert(text);
    id.innerHTML=text;
}

function backFollowData() {
    var id=document.getElementById("followTable");
    //alert("test1");
    var text=bigdata.fn.showFollowDataTable();
    //alert(text);
    id.innerHTML=text;
}

function backUserRData() {
    var id=document.getElementById("userRTable");
    //alert("test1");
    var text=userRdata.fn.showUserRDataTable();
    //alert(text);
    id.innerHTML=text;
    
}

function backItemRData() {
    var id=document.getElementById("itemRTable");
    //alert("test1");
    var text=itemRdata.fn.showItemRDataTable();
    //alert(text);
    id.innerHTML=text;
}

function backEvalData() {
    var id=document.getElementById("evalTable");
    //alert("test1");
    var text=evaldata.fn.renderMain();
    //alert(text);
    id.innerHTML=text;
}

function backFpgData() {
    var id=document.getElementById("fpgTable");
    //alert("test1");
    var text=fpgdata.fn.renderMain();
    //alert(text);
    id.innerHTML=text;
}



