<#import "/common/helpers.ftl" as helperMacros>

<#macro showCard show userShows isUserSpecific>
    <div class="col">
        <div class="card shadow-sm">
            <#if show.getBannerPath()??>
                <img src="<@s.url "/resources/" + show.getBannerPath()/>" class="card-img-top"/>
            <#else>
                <@helperMacros.imagePlaceholder />
            </#if>

            <div class="card-body">
                <p class="card-text fw-bold">${show.getName()}</p>
                <p class="card-text">
                    ${show.getSeasons()?size} Staffeln - ${showService.getTotalNumberOfEpisodes(show)} Episoden
                </p>

                <#if isUserSpecific>
                    <p class="card-text">
                        <div class="progress">
                            <div class="progress-bar" role="progressbar" style="width: 75%;"
                                 aria-valuenow="75" aria-valuemin="0" aria-valuemax="100">65/87
                            </div>
                        </div>
                    </p>
                </#if>

                <div class="d-flex justify-content-between align-items-center">
                    <a href="<@s.url "/show/" + show.getId()/>" type="button" class="btn btn-sm btn-outline-primary stretched-link">Details</a>
                    <#if isUserSpecific>
                        <a href="<@s.url "/user/shows/remove/" + show.getId()/>" type="button" class="btn btn-sm btn-outline-danger z-index-above-stretched-link">Entfernen</a>
                    <#else>
                        <a href="<@s.url "/user/shows/add/" + show.getId()/>" type="button" class="btn btn-sm btn-outline-success z-index-above-stretched-link <#if userShows?seq_contains(show)>d-none</#if>">Hinzufügen</a>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#macro>