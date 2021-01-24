let episodeLinks = document.getElementsByClassName('episodeLink');

for(let i = 0; i < episodeLinks.length; i++)
{
    episodeLinks[i].addEventListener('click', onEpisodeToggle, false);
}

function onEpisodeToggle()
{
    window.location.href = this.getAttribute('data-url');
}