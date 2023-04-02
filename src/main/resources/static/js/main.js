window.addEventListener('load', function(event)
{
    handleEpisodeToggles();
    changeSortOption();

    let toastElements = [].slice.call(document.querySelectorAll('.toast'));
    let toasts = toastElements.map(function(toastEl)
    {
        return new bootstrap.Toast(toastEl, {})
    });

    let buttonsAddImageFromApi = document.getElementsByClassName('buttonAddImage');
    for(let i = 0; i < buttonsAddImageFromApi.length; i++)
    {
        let button = buttonsAddImageFromApi[i];
        button.addEventListener('click', function()
        {
            fetchAndShowModal(button,'modal-container-add-image', 'addImageModal', initImageSelectables);
        });
    }

    let buttonsAddSeasonFromApi = document.getElementsByClassName('buttonAddSeasonFromApi');
    for(let i = 0; i < buttonsAddSeasonFromApi.length; i++)
    {
        let button = buttonsAddSeasonFromApi[i];
        button.addEventListener('click', function()
        {
            fetchAndShowModal(button,'modal-container-add-season', 'importSeasonFromApiModal', function(){});
        });
    }

    let buttonsUpdateSeasonFromApi = document.getElementsByClassName('buttonUpdateSeasonFromApi');
    for(let i = 0; i < buttonsUpdateSeasonFromApi.length; i++)
    {
        let button = buttonsUpdateSeasonFromApi[i];
        button.addEventListener('click', function()
        {
            fetchAndShowModal(button,'modal-container-update-season', 'updateSeasonFromApiModal', function(){});
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

function initImageSelectables()
{
    let imageSelectables = document.getElementsByClassName('image-selectable');
    for(let i = 0; i < imageSelectables.length; i++)
    {
        let item = imageSelectables[i];
        item.addEventListener('click', function()
        {
            let form = document.getElementsByName('imageFromApi')[0];
            let hiddenInput = form.querySelector('input[name="url"]');
            hiddenInput.value = item.src;
            form.submit();
        });
    }
}

function fetchAndShowModal(button, modalContainerId, modalId, callbackAfterModalShown)
{
    let url = button.getAttribute('data-url');

    let xhr = new XMLHttpRequest();
    xhr.open('GET', url);
    xhr.onload = function()
    {
        if(xhr.status === 200)
        {
            let modalContainer = document.getElementById(modalContainerId);
            modalContainer.innerHTML = xhr.responseText;
            let modal = new bootstrap.Modal(document.getElementById(modalId), {});
            modal.show();

            callbackAfterModalShown();
        }
        else
        {
            let modalContainer = document.getElementById(modalContainerId);
            modalContainer.innerHTML = `
                <div class="modal fade" id="${modalId}">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                ${JSON.parse(xhr.responseText).error}
                            </div>
                          </div>
                        </div>
                    </div>
                </div>`;
            let modal = new bootstrap.Modal(document.getElementById(modalId), {});
            modal.show();
        }
    };
    xhr.send();
}
