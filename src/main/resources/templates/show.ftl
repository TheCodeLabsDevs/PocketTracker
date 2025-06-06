<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>

    <@template.head show.getName()/>
    <@template.body>
        <@b.back_button url="/user/shows" center=true/>

        <div class="card shadow-sm">
            <div class="card-body">
                <h3 class="card-title text-center">${show.getName()} (${show.getFirstAired()?date('yyy-MM-dd')?string.yyyy}) <#if show.getFinished()?? && show.getFinished()>
                        <i class="fas fa-flag-checkered"></i></#if>
                </h3>
                <#if latestWatched??>
                    <div class="text-center"><@b.localize "show.lastWatched"/> ${latestWatched}</div>
                </#if>

                <@b.hasPermission "ADMIN">
                    <div class="text-center mt-2">
                        <@b.button label="button.edit" url="/show/" + show.getId() + "/edit" style="btn-sm btn-outline-primary" margin="" icon="fas fa-pencil"/>
                    </div>
                </@b.hasPermission>

                <div class="row mt-3 mt-md-5">
                    <div class="col-12 col-md-4 text-center">
                        <@b.row>
                            <@b.col>
                                <#if show.getPosterPath()??>
                                    <img src="<@s.url "/resources/" + show.getPosterPath()/>" class="img-fluid w-50"/>
                                <#else>
                                    <img src="<@s.url "/image/placeholder_poster.jpg"/>" class="img-fluid w-50"/>
                                </#if>
                            </@b.col>
                            <@b.col>
                                <#if isHidden>
                                    <@b.button url="/user/shows/toggleShowHidden/${show.getId()}" icon="fas fa-eye-slash" style="btn-danger" classes="mt-4 w-50" label="button.hidden"/>
                                <#else>
                                    <@b.button url="/user/shows/toggleShowHidden/${show.getId()}" icon="fas fa-eye-slash" style="btn-outline-danger" classes="mt-4 w-50" label="button.hide"/>
                                </#if>
                            </@b.col>
                        </@b.row>
                    </div>

                    <div class="col-12 col-md-8 mt-3 mt-md-0">
                        <div class="row">
                            <@showMacros.factItem "fas fa-folder" show.getSeasons()?size "factItem.seasons"/>
                            <@showMacros.factItem "fas fa-film" showService.getTotalNumberOfEpisodes(show) "factItem.episodes"/>
                            <@showMacros.factItem "fas fa-hourglass" showService.getTotalPlayTime(show) "factItem.duration.minutes"/>
                        </div>

                        <div class="row mt-3 mt-md-5 mb-4 mb-md-0">
                            <div class="col-12 col-md-11">
                                <div class="accordion" id="accordionDescription">
                                    <div class="accordion-item">
                                        <div class="accordion-item">
                                            <h2 class="accordion-header" id="collapseHeaderOne">
                                                <button class="accordion-button collapsed fw-bold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseItemOne" aria-expanded="false" aria-controls="collapseItemOne">
                                                    <@b.localize "show.description"/>
                                                </button>
                                            </h2>
                                            <div id="collapseItemOne" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionDescription">
                                                <div class="accordion-body">
                                                    <#if show.getDescription()??>
                                                        ${show.getDescription()}
                                                    </#if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-12 col-md-11 my-3 my-md-5">
                                <div class="list-group list-episodes">
                                    <#list show.getSeasons()?sort_by("number") as season>
                                        <a href="<@s.url "/season/" + season.getId() />" class="list-group-item list-group-item-action w-100 p-3">
                                            <div class="row">
                                                <div class="col-10 fw-bold text-truncate">
                                                    ${season.getNumber()} - ${season.getName()}
                                                </div>
                                                <div class="col-2 d-flex justify-content-end">
                                                    ${userService.getWatchedEpisodesBySeason(currentUser, season)?size}/${season.getEpisodes()?size}
                                                </div>
                                            </div>
                                            <div class="row mt-2">
                                                <div class="col">
                                                    <#assign numberOfWatchedEpisodes=userService.getWatchedEpisodesBySeason(currentUser, season)?size/>
                                                    <#assign totalNumberOfEpisodes=season.getEpisodes()?size/>
                                                    <@showMacros.progessBar numberOfWatchedEpisodes totalNumberOfEpisodes/>
                                                </div>
                                            </div>
                                        </a>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </@template.body>
</html>
