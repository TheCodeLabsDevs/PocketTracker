<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/users/form.ftl" as form>

    <@template.head show.getName()/>
    <@template.body>
        <@b.back_button center=true/>

        <div class="card shadow-sm">
            <div class="card-body">
                <@b.hasPermission "ADMIN">
                    <@b.button label="button.edit" url="/show/" + show.getId() + "/edit" style="btn-sm btn-outline-primary" classes="float-end"/>
                </@b.hasPermission>

                <h3 class="card-title text-center">${show.getName()} (${show.getFirstAired()?date('yyy-MM-dd')?string.yyyy})</h3>
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
                                <a href="<@s.url "/user/shows/add/" + show.getId()/>" role="button" class="btn btn-sm btn-outline-success mt-4 w-50 <#if isAdded>d-none</#if>">Hinzufügen</a>
                            </@b.col>
                        </@b.row>
                    </div>

                    <div class="col-12 col-md-8 mt-3 mt-md-0">
                        <div class="row">
                            <@showMacros.factItem "fas fa-folder" show.getSeasons()?size "Staffeln"/>
                            <@showMacros.factItem "fas fa-film" showService.getTotalNumberOfEpisodes(show) "Episoden"/>
                            <@showMacros.factItem "fas fa-hourglass" showService.getTotalPlayTime(show) "Minuten"/>
                        </div>

                        <div class="row mt-3 mt-md-5 mb-4 mb-md-0">
                            <div class="col-12 col-md-11">
                                <div class="accordion" id="accordionDescription">
                                    <div class="accordion-item">
                                        <div class="accordion-item">
                                            <h2 class="accordion-header" id="collapseHeaderOne">
                                                <button class="accordion-button collapsed fw-bold" type="button" data-bs-toggle="collapse" data-bs-target="#collapseItemOne" aria-expanded="false" aria-controls="collapseItemOne">
                                                    Beschreibung
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
