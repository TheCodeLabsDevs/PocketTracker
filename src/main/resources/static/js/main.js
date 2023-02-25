window.addEventListener('load', function(event)
{
    handleEpisodeToggles();
    changeSortOption();

    let toastElements = [].slice.call(document.querySelectorAll('.toast'));
    let toasts = toastElements.map(function(toastEl)
    {
        return new bootstrap.Toast(toastEl, {})
    });

    let buttonAddPosterImageFromApi = document.getElementById('buttonAddPoster');
    if(buttonAddPosterImageFromApi !== null)
    {
        buttonAddPosterImageFromApi.addEventListener('click', function()
        {
            fetchPosterImagesAndShowModal(buttonAddPosterImageFromApi);
        });
    }
});

function handleEpisodeToggles()
{
    let episodeLinks = document.getElementsByClassName('episodeLink');
    for(let i = 0; i < episodeLinks.length; i++)
    {
        episodeLinks[i].addEventListener('click', onEpisodeToggle, false);
    }

    function onEpisodeToggle(event)
    {
        let element = this;
        let url = element.getAttribute('data-url');

        let xhr = new XMLHttpRequest();
        xhr.open('GET', url);
        xhr.onload = function()
        {
            if(xhr.status === 200)
            {
                let toastElement = document.getElementById('toast-success');
                bootstrap.Toast.getOrCreateInstance(toastElement).show();
            }
            else
            {
                console.error('Request failed! Status code: ' + xhr.status);

                let toastElement = document.getElementById('toast-danger');
                bootstrap.Toast.getOrCreateInstance(toastElement).show();

                event.preventDefault();
            }
        };
        xhr.send();
    }
}

function changeSortOption()
{
    let showDislikedShowsLink = document.getElementById("showDislikedShows");
    let showDislikedShowsInput = document.getElementsByName("showDislikedShows")[0];
    if(showDislikedShowsLink)
    {
        showDislikedShowsLink.onclick = function()
        {
            showDislikedShowsInput.value = !((showDislikedShowsInput.value === 'true'));
            document.getElementById("sortOptionForm").submit();
        };
    }

    let sortOption = document.getElementById("sortOption");
    if(sortOption)
    {
        sortOption.addEventListener("change", onChange, false);
    }

    let filterOption = document.getElementById("filterOption");
    if(filterOption)
    {
        filterOption.addEventListener("change", onChange, false);
    }

    function onChange(event)
    {
        document.getElementById("sortOptionForm").submit();
    }
}

function fetchPosterImagesAndShowModal(button)
{
    let url = button.getAttribute('data-url');

    let xhr = new XMLHttpRequest();
    xhr.open('GET', url);
    xhr.onload = function()
    {
        if(xhr.status === 200)
        {
            let modalContainer = document.getElementById('modal-container-add-poster');
            modalContainer.innerHTML = xhr.responseText;
            let modal = new bootstrap.Modal(document.getElementById('addPoster'), {});
            modal.show();
            initImageSelectables();
        }
        else
        {
            console.error('Request failed! Status code: ' + xhr.status);
        }
    };
    xhr.send();
}

function initImageSelectables()
{
    let imageSelectables = document.getElementsByClassName('image-selectable');
    for(let i = 0; i < imageSelectables.length; i++)
    {
        let item = imageSelectables[i];
        item.addEventListener('click', function()
        {
            let form = document.getElementsByName('posterFromApi')[0];
            let hiddenInput = form.querySelector('input[name="url"]');
            hiddenInput.value = item.src;
            form.submit();
        });
    }
}