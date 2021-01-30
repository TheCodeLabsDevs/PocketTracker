<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/users/form.ftl" as form>

    <@template.head showService.getShortCode(episode) + " - " + episode.getName()/>
    <@template.body>
        <div class="mx-auto text-center mb-4">
            <a href="<@s.url "/season/" + episode.getSeason().getId()?c/>" class="btn btn-primary" role="button"><i class="fas fa-arrow-left"></i> Back</a>
        </div>

        <div class="card shadow-sm">
            <div class="card-body">
                <h2 class="card-title text-center mb-2 text-truncate">${episode.getSeason().getShow().getName()}</h2>
                <h3 class="card-title text-center">
                    ${showService.getShortCode(episode)} - ${episode.getName()}
                    <input class="form-check-input fs-3 ms-2 episodeLink" data-url="<@s.url "/user/episode/" + episode.getId()?c + "/toggle/episode"/>" type="checkbox" value="" <#if userService.isWatchedEpisode(currentUser, episode)>checked</#if>>
                </h3>

                <div class="row mt-3 mt-md-5">
                    <div class="col-12 mt-3 mt-md-0">
                        <div class="row">
                            <@showMacros.factItem "fas fa-calendar" episode.getFirstAired()?date('yyy-MM-dd') "Datum"/>
                            <#if episode.getLengthInMinutes()??>
                                <@showMacros.factItem "fas fa-hourglass" episode.getLengthInMinutes() "Minuten"/>
                            </#if>
                        </div>

                        <#if episode.getDescription()??>
                            <div class="row mt-3 mt-md-5 mb-4 mb-md-0">
                                <div class="col-12 col-md-8 offset-md-2">
                                    <h5>Beschreibung</h5>
                                    ${episode.getDescription()}
                                </div>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </@template.body>
</html>

