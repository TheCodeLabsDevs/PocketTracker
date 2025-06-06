<!doctype html>
<html>
    <#import "/spring.ftl" as s/>

    <#import "/common/template.ftl" as template>
    <#import "/common/components/base.ftl" as b/>
    <#import "/common/macros/movie.ftl" as movieMacros/>
    <#import "/common/components/modal.ftl" as m>
    <#import "/common/components/form.ftl" as f>

    <@template.head movie.getName()/>
    <@template.body>
        <@b.back_button url="/user/movies" center=true/>

        <div class="card shadow-sm">
            <div class="card-body">
                <h3 class="card-title text-center">${movie.getName()} <#if movie.getReleaseDate()??>(${movie.getReleaseDate()?date('yyy-MM-dd')?string["dd.MM.yyyy"]})</#if></h3>

                <#if userService.getWatchDateForMovie(currentUser, movie)??>
                    <div class="text-center d-flex align-items-center justify-content-center">
                        <@b.localize "movie.lastWatched"/> ${userService.getWatchDateForMovie(currentUser, movie)}
                        <button type="button" class="btn btn-flat text-primary" data-bs-toggle="modal" data-bs-target="#modal-edit-last-watched-date">
                            <i class="fas fa-calendar-alt"></i> <@b.localize "button.edit.lastWatchedDate"/>
                        </button>
                    </div>

                    <@m.modal id='modal-edit-last-watched-date'>
                        <@f.form name="modal-edit-last-watched-date-form" url='/movie/' + movie.getId() + '/updateLastWatchedDate'>
                            <@m.header 'movie.modal.lastWatched.edit.title'/>
                            <@m.body>
                                <@b.row>
                                    <@b.col>
                                        <@f.input objectName='lastWatchedDate' label="movie.lastWatched" name="lastWatchedDate" type="date" value="${userService.getWatchDateForMovieISO(currentUser, movie)}"/>
                                    </@b.col>
                                </@b.row>
                            </@m.body>
                            <@m.footer>
                                <@m.cancelButton/>
                                <@f.submit label='button.save' col=false style="btn-primary" icon="fas fa-save"/>
                            </@m.footer>
                        </@f.form>
                    </@m.modal>
                </#if>

                <@b.hasPermission "ADMIN">
                    <div class="text-center mt-2">
                        <@b.button label="button.edit" url="/movie/" + movie.getId() + "/edit" style="btn-sm btn-outline-primary" margin="" icon="fas fa-pencil"/>
                    </div>
                </@b.hasPermission>

                <div class="row mt-3 mt-md-5 mb-5">
                    <div class="col-12 col-md-4 text-center">
                        <@b.row>
                            <@b.col>
                                <#if movie.getPosterPath()??>
                                    <img src="<@s.url "/resources/" + movie.getPosterPath()/>" class="img-fluid w-50"/>
                                <#else>
                                    <img src="<@s.url "/image/placeholder_poster.jpg"/>" class="img-fluid w-50"/>
                                </#if>
                            </@b.col>
                            <@b.col>
                                <#if userService.isMovieAdded(currentUser, movie.id)>
                                    <@b.button url="/user/movies/remove/" + movie.getId() icon="fas fa-check" style="btn-success" classes="mt-4 w-50" label="button.watched"/>
                                <#else>
                                    <@b.button url="/user/movies/add/" + movie.getId() icon="fas fa-check" style="btn-outline-success" classes="mt-4 w-50" label="button.watch"/>
                                </#if>
                            </@b.col>
                        </@b.row>
                    </div>

                    <div class="col-12 col-md-8 mt-3 mt-md-0">
                        <div class="row">
                            <@movieMacros.factItem "fas fa-hourglass" movie.getLengthInMinutes() "factItem.duration.minutes"/>
                        </div>

                        <div class="row mt-3 mt-md-5 mb-4 mb-md-0">
                            <div class="col-12 col-md-11">

                                <h4><@b.localize "movie.description"/></h4>
                                <#if movie.getDescription()??>
                                    ${movie.getDescription()}
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </@template.body>
</html>
