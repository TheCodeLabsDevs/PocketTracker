<#import "/spring.ftl" as s/>
<#import "/common/components/base.ftl" as b/>

<#macro showCard show>
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

                <div class="mb-4">
                    <#assign numberOfWatchedEpisodes=userService.getWatchedEpisodesByShow(currentUser, show)?size/>
                    <#assign totalNumberOfEpisodes=showService.getTotalNumberOfEpisodes(show)/>
                    <@progessBar numberOfWatchedEpisodes totalNumberOfEpisodes/>
                </div>

                <div class="d-flex justify-content-between align-items-center">
                    <a href="<@s.url "/show/" + show.getId()/>" role="button" class="button-square btn btn-sm btn-outline-primary stretched-link"><i class="fas fa-info"></i></a>

                    <div class="d-flex justify-content-end">
                        <@b.hasPermission "ADMIN">
                            <a href="<@s.url "/show/" + show.getId() + "/edit"/>" role="button" class="button-square btn btn-sm btn-outline-secondary z-index-above-stretched-link"><i class="fas fa-pencil"></i></a>
                        </@b.hasPermission>
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