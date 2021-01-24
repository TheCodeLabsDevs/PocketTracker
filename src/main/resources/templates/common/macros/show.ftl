<#import "/spring.ftl" as s/>
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
                    <div class="mb-4">
                        <#assign numberOfWatchedEpisodes=userService.getWatchedEpisodesByShow(currentUser, show)?size/>
                        <#assign totalNumberOfEpisodes=showService.getTotalNumberOfEpisodes(show)/>
                        <@progessBar numberOfWatchedEpisodes totalNumberOfEpisodes/>
                    </div>
                </#if>

                <div class="d-flex justify-content-between align-items-center">
                    <a href="<@s.url "/show/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-primary stretched-link">Details</a>
                    <#if isUserSpecific>
                        <a href="<@s.url "/user/shows/remove/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-danger z-index-above-stretched-link">Entfernen</a>
                    <#else>
                        <a href="<@s.url "/user/shows/add/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-success z-index-above-stretched-link <#if userShows?seq_contains(show)>d-none</#if>">Hinzufügen</a>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro progessBar currentValue totalValue>
    <div class="progress">
        <#if currentValue==0 || totalValue==0>
            <#assign progress = 0/>
        <#else>
            <#assign progress=currentValue / totalValue * 100/>
        </#if>
        <div class="progress-bar <#if progress == 100>bg-success</#if>" role="progressbar" style="width: ${progress?c}%;" aria-valuenow="currentValue" aria-valuemin="0" aria-valuemax="totalValue">${currentValue}/${totalValue}</div>
    </div>
</#macro>