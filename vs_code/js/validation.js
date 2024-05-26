// VALIDATION
function Validator(job) {
	var workplace = document.querySelector(job.workplace);
	var listRule = {};
	
	workplace.onsubmit = function(e) {
		e.preventDefault();
		
		job.rules.forEach(function(rule) {
			var workplaceElement = workplace.querySelector(rule.select);
			
			validate(workplaceElement, rule);
		});

        workplace.querySelectorAll(".invalid-feedback").forEach(function(element){
            element.classList.add("d-block")
        })
	}
	
	function getParent(workplaceElement, parent) {
		while(workplaceElement.parentElement){
			if(workplaceElement.parentElement.matches(parent)) {
				return workplaceElement.parentElement;
			}
			workplaceElement = workplaceElement.parentElement;
		}
	}
	
	function validate(workplaceElement, rule) {
		var errorMessage = "";
		for(var i = 0; i < listRule[rule.select].length; i++){
			switch(workplaceElement.type){
					case "radio":
					case "checkbox":
						errorMessage = listRule[rule.select][i](workplace.querySelector(rule.select + ":checked") ? workplace.querySelector(rule.select + ":checked").value : undefined)
						break; 
					default:
						errorMessage = listRule[rule.select][i](workplaceElement.value)
						break;
					
			};
			if(errorMessage){
				break;
			}
		}
		
		if(errorMessage){
			getParent(workplaceElement, job.formGroupSelector).querySelector(".invalid-feedback").innerHTML = errorMessage;
			getParent(workplaceElement, job.formGroupSelector).querySelector(".invalid-feedback").style.color = "red";
		} else {
			getParent(workplaceElement, job.formGroupSelector).querySelector(".invalid-feedback").innerHTML = "";
		}
		
		return errorMessage;
	}
	
	job.rules.forEach(function(rule) {
		var workplaceElements = workplace.querySelectorAll(rule.select);
		
		if(Array.isArray(listRule[rule.select])){
			listRule[rule.select].push(rule.test)
		} else {
			listRule[rule.select] = [rule.test];
		}
		
		Array.from(workplaceElements).forEach(function(workplaceElement){
			workplaceElement.onblur = function() {
				validate(workplaceElement, rule);

                workplace.querySelectorAll(".invalid-feedback").forEach(function(element){
                    element.classList.add("d-block")
                })
			};
            
            workplaceElement.oninput = function () {
                var errorElement = getParent(workplaceElement, job.formGroupSelector).querySelector(".invalid-feedback");
                errorElement.innerText = "";
            }       
		})
	})
}

// RULE
Validator.isRequired = function(select, message) {
	return {
		select: select,
		test: function(value) {
			return value ? undefined : message || "Vui lòng nhập trường này";
		}
	}
}

Validator.isEmail = function(select, message) {
	return {
		select: select,
		test: function(value) {
			var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			return regex.test(value) ? undefined :  message || 'Trường này phải là email';
		}
	}
}


Validator.isLengthMin = function(select, minValue, message) {
	return {
		select: select,
		test: function(value) {
			var min = minValue ? minValue : 6
			return value.length >= min ? undefined :  message || `Trường này phải chứa ít nhất ${min} ký tự`;
		}
	}
}

Validator.isConfirm = function(select, confirm, message) {
	return {
		select: select,
		test: function(value) {
			return value == confirm() ? undefined :  message || `Trường này nhập không đúng`;
		}
	}
}


/*

*/
// Validation password
var password = document.querySelector('.password');

document.querySelector('.password').onfocus = function (params) {
	document.querySelector(".valid-password").classList.remove("d-none");
}

document.querySelector('.password').onblur = function (params) {
	document.querySelector(".valid-password").classList.add("d-none");
}

var validPassword = {
	charLength: document.querySelector('.valid-password .length'),
	lowercase: document.querySelector('.valid-password .lowercase'),
	uppercase: document.querySelector('.valid-password .uppercase'),
	number: document.querySelector('.valid-password .number'),
	special: document.querySelector('.valid-password .special')
};

var pattern = {

	charLength: function () {
		if (password.value.length >= 8 && password.value.length <= 20) {
			return true;
		}
	},

	lowercase: function () {
		var regex = /^(?=.*[a-z]).+$/; // Lowercase character pattern
		if (regex.test(password.value)) {
			return true;
		}
	},

	uppercase: function () {
		var regex = /^(?=.*[A-Z]).+$/; // Uppercase character pattern
		if (regex.test(password.value)) {
			return true;
		}
	},

	number: function () {
		var regex = /^(?=.*[0-9]).+$/; // Number check
		if (regex.test(password.value)) {
			return true;
		}
	},

	special: function () {
		var regex = /^(?=.*[_\W]).+$/; // Special character 
		if (regex.test(password.value)) {
			return true;
		}
	}
};

// Listen for keyup action on password field
password.addEventListener('keyup', function () {
	patternTest(pattern.charLength(), validPassword.charLength);
	patternTest(pattern.lowercase(), validPassword.lowercase);
	patternTest(pattern.uppercase(), validPassword.uppercase);
	patternTest(pattern.number(), validPassword.number);
	patternTest(pattern.special(), validPassword.special);

	// Check that all requirements are fulfilled
	if (hasClass(validPassword.charLength, 'valid')) {
		var i = 0;
		if (document.querySelectorAll(".true3in4 li").forEach(e => {
			if (e.classList[1] === "valid") {
				i++;
			}
		}))

		console.log(i >= 3)
		if (i >= 3) {
			addClass(password.parentElement, 'valid');
			document.querySelector(".batbuoc").classList.add("d-none");
			document.querySelector(".true3in4").classList.add("d-none");

			document.querySelector(".trueValid").classList.remove("d-none");
		} else {
			removeClass(password.parentElement, 'valid');
			document.querySelector(".batbuoc").classList.remove("d-none");
			document.querySelector(".true3in4").classList.remove("d-none");

			document.querySelector(".trueValid").classList.add("d-none");
		}
	}
	else {
		removeClass(password.parentElement, 'valid');
		document.querySelector(".batbuoc").classList.remove("d-none");
		document.querySelector(".true3in4").classList.remove("d-none");

		document.querySelector(".trueValid").classList.add("d-none");
	}
});


// Pattern Test function
function patternTest(pattern, response) {
	if (pattern) {
		addClass(response, 'valid');
	}
	else {
		removeClass(response, 'valid');
	}
}

// Has Class Function 
function hasClass(el, className) {
	if (el.classList) {
		return el.classList.contains(className);
	}
	else {
		new RegExp('(^| )' + className + '( |$)', 'gi').test(el.className);
	}
}

// Add Class Function
function addClass(el, className) {
	if (el.classList) {
		el.classList.add(className);
	}
	else {
		el.className += ' ' + className;
	}
}

// Remove Class Function
function removeClass(el, className) {
	if (el.classList)
		el.classList.remove(className);
	else
		el.className = el.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
}