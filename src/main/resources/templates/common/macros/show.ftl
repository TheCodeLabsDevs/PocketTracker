<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>

<#macro showCard show userShows isUserSpecific>
    <div class="col">
        <div class="card shadow-sm">
            <#if show.getBannerPath()??>
                <img src="<@s.url "/resources/" + show.getBannerPath()/>" class="card-img-top"/>
            <#else>
                <img src="<@s.url "/image/placeholder_banner.jpg"/>" class="card-img-top"/>
            </#if>

            <div class="card-body">
                <p class="card-text fw-bold">
                    ${show.getName()}
                </p>
                <p class="card-text">
                   ${show.getSeasons()?size} <@b.localize "index.seasons"/> - ${showService.getTotalNumberOfEpisodes(show)} <@b.localize "index.episodes"/> <#if show.getFinished()?? && show.getFinished()><i class="fas fa-flag-checkered"></i></#if>
                </p>

                <#if isUserSpecific>
                    <div class="mb-4">
                        <#assign numberOfWatchedEpisodes=userService.getWatchedEpisodesByShow(currentUser, show)?size/>
                        <#assign totalNumberOfEpisodes=showService.getTotalNumberOfEpisodes(show)/>
                        <@progessBar numberOfWatchedEpisodes totalNumberOfEpisodes/>
                    </div>
                </#if>

                <div class="d-flex justify-content-between align-items-center">
                    <a href="<@s.url "/show/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-primary stretched-link"><@b.localize "button.details"/></a>

                    <div class="d-flex justify-content-end">
                        <#if isUserSpecific>
                            <a href="<@s.url "/user/shows/remove/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-danger z-index-above-stretched-link"><@s.message "button.remove"/></a>
                        <#else>
                            <@b.hasPermission "ADMIN">
                                <a href="<@s.url "/show/" + show.getId() + "/edit"/>" role="button" class="btn btn-sm btn-outline-danger z-index-above-stretched-link"><@b.localize "button.edit"/></a>
                            </@b.hasPermission>
                            <a href="<@s.url "/user/shows/add/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-success z-index-above-stretched-link <#if userShows?seq_contains(show)>d-none</#if> ms-2"><@s.message "button.add"/></a>
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
        <div class="progress-bar <#if progress == 100>bg-success</#if>" role="progressbar" style="width: ${progress?c}%;" aria-valuenow="currentValue" aria-valuemin="0" aria-valuemax="totalValue">${currentValue}/${totalValue}</div>
    </div>
</#macro>

<#macro factItem icon value description classes="" localizedDescription=true>
    <div class="col text-center ${classes}">
        <i class="${icon} fs-4"></i>
        <div class="fs-5">
            ${value}&nbsp;
            <span class="d-md-none"><br></span>
            <#if localizedDescription>
                <@b.localize description/>
            <#else>
                ${description}
            </#if>
        </div>
    </div>
</#macro>