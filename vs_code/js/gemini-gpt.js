const API_KEY = "AIzaSyC26q4osHMCUH-bfIbjppqJ_RST-rf5ppg";
const API_URL = `https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=${API_KEY}`;
const submitButton = document.querySelector('#btn-gpt')
const loadWrapp = document.querySelector(".load-wrapp")

async function getMessage() {
    console.log("clicked")

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                contents: [{
                    role: "user",
                    parts: [{ text: "Hãy giới thiệu về bạn đi" }]
                }]
            })
        });

        const data = await response.json();

        console.log(data?.candidates[0].content.parts[0].text);
        loadWrapp.classList.add("d-none")
        loadWrapp.classList.remove("d-flex")
        document.querySelector(".descriptions").innerHTML = `<div class="ql-editor" contenteditable="true"><p class="">${data?.candidates[0].content.parts[0].text}</p></div>`
    } catch (error) {
        console.log(error);
    }
}


submitButton.addEventListener('click', () => {
    loadWrapp.classList.remove("d-none")
    loadWrapp.classList.add("d-flex")
    document.querySelector(".ql-editor").classList.add("d-flex")
    document.querySelector(".ql-editor").classList.add("justify-content-center") 
    document.querySelector(".ql-editor").classList.add("fs-5")
    document.querySelector(".ql-editor").classList.add("letter-holder")
    
    var i = 0;
    document.querySelectorAll(".ql-editor.d-flex p").forEach(e => {
        i++;
        var s = "l-"+ i
        e.classList.add(s)
        e.classList.add("letter")
        console.log(e.classList)
    }) 

    setTimeout(() => {
        getMessage(); 
    }, 2000)
});
