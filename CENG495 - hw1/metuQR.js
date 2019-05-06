var getQR = function () {
    
	var qr = document.getElementById("qr");
	var color = document.querySelector('#d1').value;
	var bg = document.querySelector('#d2').value;

	qr.innerHTML = `<img id='barcode' 
	src="https://api.qrserver.com/v1/create-qr-code/?data=${encodeURIComponent(document.getElementById("textToQR").value)}&size=200x200&color=${encodeURIComponent(color)}&bgcolor=${encodeURIComponent(bg)}"/>
	`;

	qr.classList.add("qr-active");
}