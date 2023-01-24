window.addEventListener('load', function (event) {
    document.getElementById("searchShowName").addEventListener('input', debounce(updateSearchRequests));
});

function debounce(func, delay = 250) {
    let timerId;
    return (...args) => {
        clearTimeout(timerId);
        timerId = setTimeout(() => {
            func.apply(this, args);
        }, delay);
    };
}

function updateSearchRequests(event) {
    let resultContainer = document.getElementById("searchResultContainer");

    let searchValue = event.target.value;
    if (searchValue === '') {
        resultContainer.innerHTML = '';
        return;
    }

    const httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function () {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                resultContainer.innerHTML = httpRequest.responseText;
            }
        }
    };
    httpRequest.open('POST', `${baseUrl}show/searchApi`, true);

    let token = document.querySelector("meta[name='_csrf']").attributes["content"].value;
    let header = document.querySelector("meta[name='_csrf_header']").attributes["content"].value;
    httpRequest.setRequestHeader(header, token);

    httpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")
    httpRequest.send(`type=${document.getElementById('type').value}&search=${searchValue}`);
}