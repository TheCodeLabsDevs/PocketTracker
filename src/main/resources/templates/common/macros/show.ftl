<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>
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
                <p class="card-text fw-bold">
                    ${show.getName()}
                </p>
                <p class="card-text">
                   ${show.getSeasons()?size} Staffeln - ${showService.getTotalNumberOfEpisodes(show)} Episoden <i class="fas fa-flag-checkered"></i>
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

                    <div class="d-flex justify-content-end">
                        <#if isUserSpecific>
                            <a href="<@s.url "/user/shows/remove/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-danger z-index-above-stretched-link">Entfernen</a>
                        <#else>
                            <@b.hasPermission "ADMIN">
                                <a href="<@s.url "/show/" + show.getId() + "/edit"/>" role="button" class="btn btn-sm btn-outline-danger z-index-above-stretched-link"><@b.localize "button.edit"/></a>
                            </@b.hasPermission>
                            <a href="<@s.url "/user/shows/add/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-success z-index-above-stretched-link <#if userShows?seq_contains(show)>d-none</#if> ms-2">Hinzufügen</a>
                        </#if>
                    </div>
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
        <div class="progress-bar <#if progress == 100>bg-success</#if>" role="progressbar" style="width: ${progress}%;" aria-valuenow="currentValue" aria-valuemin="0" aria-valuemax="totalValue">${currentValue}/${totalValue}</div>
    </div>
</#macro>

<#macro factItem icon value description classes="">
    <div class="col text-center ${classes}">
        <i class="${icon} fs-4"></i>
        <div class="fs-5">
            ${value}&nbsp;
            <span class="d-md-none"><br></span>
            ${description}
        </div>
    </div>
</#macro>