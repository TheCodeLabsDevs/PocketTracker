<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/template.ftl" as template>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/common/components/base.ftl" as b/>

    <@template.head season.getName() + " - " + season.getShow().getName()/>

    <@template.body>
        <div class="row mb-4">
            <div class="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                <@b.back_button url="/show/" + season.getShow().getId() center=true/>
                <h2 class="mb-2 text-truncate">${season.getShow().getName()}</h2>
                <h4>-${season.getName()}-</h4>
            </div>
        </div>

        <div class="text-center">
            <@b.hasPermission "ADMIN">
                <@b.button label="button.edit" url="/season/" + season.getId() + "/edit" style="btn-sm btn-outline-primary" icon="fas fa-pencil"/>
            </@b.hasPermission>
        </div>

        <div class="row mb-4">
            <div class="col-sm-12 col-md-8 col-lg-6 mx-auto text-center" id="progress-bar-container">
                <#assign numberOfWatchedEpisodes=userService.getWatchedEpisodesBySeason(currentUser, season)?size/>
                <#assign totalNumberOfEpisodes=season.getEpisodes()?size/>
                <@showMacros.progessBar numberOfWatchedEpisodes totalNumberOfEpisodes/>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                <@b.button label="button.markAllWatched" url="/user/season/" + season.getId() + "?markAsWatched=true" icon="fas fa-check" style="btn-outline-success"/>
                <@b.button label="button.markAllUnwatched" url="/user/season/" + season.getId() + "?markAsWatched=false" icon="fas fa-ban" style="btn-outline-danger"/>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-12 col-md-8 col-lg-6 mx-auto">
                <div class="list-group list-episodes">
                    <#list season.getEpisodes()?sort_by("number") as episode>
                        <div class="list-group-item list-group-item-action w-100 p-3">
                            <div class="row">
                                <a href="<@s.url "/episode/" + episode.getId()/>" class="col-10 fw-bold text-truncate text-decoration-none text-body">
                                    ${episode.getNumber()} - ${episode.getName()}
                                </a>

                                <div class="col-2 d-flex justify-content-end">
                                    <input class="form-check-input episodeLink" data-url="<@s.url "/user/episode/" + episode.getId() + "/toggle"/>" type="checkbox" value="" <#if userService.isWatchedEpisode(currentUser, episode)>checked</#if>>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
        </div>

        <div id="toast-success" class="toast align-items-center text-white bg-success border-0 position-fixed bottom-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body"><@b.localize "toast.episode.toggle.status.success"/></div>
            </div>
        </div>
        <div id="toast-danger" class="toast align-items-center text-white bg-danger border-0 position-fixed bottom-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body"><@b.localize "toast.episode.toggle.status.error"/></div>
            </div>
        </div>
    </@template.body>
</html>
