window.addEventListener('load', function(event)
{
    handleEpisodeToggles();
    changeSortOption();
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
        window.location.href = this.getAttribute('data-url');
        event.preventDefault();
    }
}

function changeSortOption() {
    let sortOption = document.getElementById("sortOption");
    if (sortOption) {
        sortOption.addEventListener("change", onChange, false);
    }

    let filterOption = document.getElementById("filterOption");
    if (filterOption) {
        filterOption.addEventListener("change", onChange, false);
    }

    function onChange(event) {
        document.getElementById("sortOptionForm").submit();
    }
}