<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/show.ftl" as showMacros/>
    <#import "/users/form.ftl" as form>

    <@template.head helpers.getShortCode(episode) + " - " + episode.getName()/>
    <@template.body>
        <@b.back_button url="/season/" + episode.getSeason().getId() center=true/>

        <div class="card shadow-sm">
            <div class="card-body">
                <h2 class="card-title text-center mb-2 text-truncate">${episode.getSeason().getShow().getName()}</h2>
                <h3 class="card-title text-center">
                    ${helpers.getShortCode(episode)} - ${episode.getName()}
                    <input class="form-check-input fs-3 ms-2 episodeLink" data-url="<@s.url "/user/episode/" + episode.getId() + "/toggle/episode"/>" type="checkbox" value="" <#if userService.isWatchedEpisode(currentUser, episode)>checked</#if>>
                </h3>

                <div class="text-center">
                    <@b.hasPermission "ADMIN">
                        <@b.button label="button.edit" url="/episode/" + episode.getId() + "/edit" style="btn-sm btn-outline-primary"/>
                    </@b.hasPermission>
                </div>

                <div class="row mt-3 mt-md-5">
                    <div class="col-12 col-md-4 text-center">
                        <@b.row>
                            <@b.col>
                                <#if episode.getPosterPath()??>
                                    <img src="<@s.url "/resources/" + episode.getPosterPath()/>" class="img-fluid w-50"/>
                                </#if>
                            </@b.col>
                        </@b.row>
                    </div>

                    <div class="col-12 col-md-8 <#if episode.getPosterPath()??>mt-3<#else>offset-md-2</#if>">
                        <div class="row">
                            <#if episode.getFirstAired()??>
                                <@showMacros.factItem "fas fa-calendar" episode.getFirstAired()?date('yyy-MM-dd') "Datum"/>
                            </#if>
                            <#if episode.getLengthInMinutes()?? && episode.getLengthInMinutes() \gt 0>
                                <@showMacros.factItem "fas fa-hourglass" episode.getLengthInMinutes() "Minuten"/>
                            </#if>
                        </div>

                        <#if episode.getDescription()??>
                            <div class="row mt-3 mt-md-5 mb-4 mb-md-0">
                                <div class="col-12 <#if episode.getPosterPath()??>col-md-11<#else>col-md-8 offset-md-2</#if>">
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

