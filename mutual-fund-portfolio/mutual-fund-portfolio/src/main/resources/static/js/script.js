document.addEventListener('DOMContentLoaded', function() {
	const userIconButton = document.querySelector('.user-icon-button');

	let storedColor = getCookie('userIconColor');

	if (!storedColor) {
		storedColor = getRandomColor();
		setCookie('userIconColor', storedColor, 7);
	}

	userIconButton.style.backgroundColor = storedColor;
});

function getRandomColor() {
	const letters = '0123456789ABCDEF';
	let color = '#';
	for (let i = 0; i < 6; i++) {
		color += letters[Math.floor(Math.random() * 16)];
	}
	return color;
}

function setCookie(name, value, days) {
	let expires = '';
	if (days) {
		const date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		expires = '; expires=' + date.toUTCString();
	}
	document.cookie = name + '=' + value + expires + '; path=/mutual-fund-portfolio';
}

function getCookie(name) {
	const nameEQ = name + '=';
	const ca = document.cookie.split(';');

	for (const cookie of ca) {
		let c = cookie.trim();
		if (c.startsWith(nameEQ)) {
			return c.substring(nameEQ.length);
		}
	}

	return null;
}

function confirmLogout() {
	var logoutButton = document.getElementById("logoutButton");

    // Check if the button is already disabled
    if (logoutButton.disabled) {
        return;
    }

    logoutButton.disabled = true;
    logoutButton.innerHTML = "Logout";

    toastr.options = {
        closeButton: true,
        progressBar: true,
        positionClass: 'toast-top-center',
        timeOut: '0',
        extendedTimeOut: '0',
        tapToDismiss: false,
		backgroundColor: 'black',
		color: 'white',
		onHidden: function () {
            // Enable the Logout Button after the Toastr notification is hidden
            logoutButton.disabled = false;
            logoutButton.innerHTML = "Logout";
        }
    };

    var logoutUrl = `/logout`;

    toastr.warning(
        'Are you sure you want to logout? <br/><br/><button type="button" class="btn clear" id="okBtn">OK</button>&nbsp;&nbsp;<button type="button" class="btn clear toastr-no" id="cancelBtn">Cancel</button>',
        'Logout Confirmation', {
            allowHtml: true,
            onclick: function (toast) {
            },
            onCloseClick: function () {
            }
        }
    );

    $('#okBtn').click(function () {
        window.location.href = logoutUrl;
    });

    $('#cancelBtn').click(function () {
		logoutButton.disabled = false;
        logoutButton.innerHTML = "Logout";
		// window.location.href = "";
		toastr.remove();
    });

	$('.toastr-no').css('backgroundColor', 'red');
}

document.addEventListener('DOMContentLoaded', function() {
	var popup = document.getElementById('popup');

	if (popup.innerText.trim() !== '') {
		popup.style.display = 'block';

		setTimeout(function() {
			popup.style.display = 'none';
		}, 5000); // Adjust the time duration as needed
	}
});