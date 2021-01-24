<!doctype html>
<html>
    <#import "/spring.ftl" as s/>
    <#import "/common/helpers.ftl" as helperMacros>
    <#import "/common/template.ftl" as template>
    <#import "/common/macros/show.ftl" as showMacros/>

    <@template.head season.getName()/>

    <body class="bg-light">
        <#import "/common/navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="container mt-5">
                <div class="row mb-4">
                    <div class="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                        <button class="btn btn-primary mb-4" type="button"><i class="fas fa-arrow-left"></i> Back</button>
                        <h2 class="mb-2 text-truncate">${season.getShow().getName()}</h2>
                        <h4 class="mb-4">-${season.getName()}-</h4>

                        <#assign numberOfWatchedEpisodes=userService.getWatchedEpisodesBySeason(currentUser, season)?size/>
                        <#assign totalNumberOfEpisodes=season.getEpisodes()?size/>
                        <@showMacros.progessBar numberOfWatchedEpisodes totalNumberOfEpisodes/>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-12 col-md-8 col-lg-6 mx-auto">
                        <div class="list-group list-episodes">
                            <a href="#" class="list-group-item list-group-item-action w-100 p-3">
                                <div class="row">
                                    <div class="col-10 fw-bold text-truncate">
                                        1 - Lorem Ipsum
                                    </div>
                                    <div class="col-2 d-flex justify-content-end">
                                        <input class="form-check-input" type="checkbox" value="" checked>
                                    </div>
                                </div>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action w-100 p-3">
                                <div class="row">
                                    <div class="col-10 fw-bold text-truncate">
                                        2 - dolor sit amet incognito da
                                    </div>
                                    <div class="col-2 d-flex justify-content-end">
                                        <input class="form-check-input" type="checkbox" value="" checked>
                                    </div>
                                </div>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action w-100 p-3">
                                <div class="row">
                                    <div class="col-10 fw-bold text-truncate">
                                        3 - asads sadsudsua asdouias
                                    </div>
                                    <div class="col-2 d-flex justify-content-end">
                                        <input class="form-check-input" type="checkbox" value="">
                                    </div>
                                </div>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action w-100 p-3">
                                <div class="row">
                                    <div class="col-10 fw-bold text-truncate">
                                        4 - Whatever deluxe
                                    </div>
                                    <div class="col-2 d-flex justify-content-end">
                                        <input class="form-check-input" type="checkbox" value="">
                                    </div>
                                </div>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action w-100 p-3">
                                <div class="row">
                                    <div class="col-10 fw-bold text-truncate">
                                        5 - Spannend, wie nie zuvor
                                    </div>
                                    <div class="col-2 d-flex justify-content-end">
                                        <input class="form-check-input" type="checkbox" value="">
                                    </div>
                                </div>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action w-100 p-3">
                                <div class="row">
                                    <div class="col-10 fw-bold text-truncate">
                                        6 - was ein endlos langer episoden name soll das sein du rieseneimer lorem ipsum
                                    </div>
                                    <div class="col-2 d-flex justify-content-end">
                                        <input class="form-check-input" type="checkbox" value="">
                                    </div>
                                </div>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action w-100 p-3">
                                <div class="row">
                                    <div class="col-10 fw-bold text-truncate">
                                        7 - as465d$%&
                                    </div>
                                    <div class="col-2 d-flex justify-content-end">
                                        <input class="form-check-input" type="checkbox" value="">
                                    </div>
                                </div>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </body>
</html>
