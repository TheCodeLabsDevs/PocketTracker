window.addEventListener('load', function(event)
{
    handleEpisodeToggles();
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