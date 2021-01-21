<!doctype html>
<html>
    <#import "spring.ftl" as s/>
    <#import "helpers.ftl" as helperMacros>

    <#import "/common/head.ftl" as headMacros>
    <@headMacros.head 'Episodes'/>

    <body class="bg-light">
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar/>

        <main>
            <div class="container mt-5">
                <div class="row mb-4">
                    <div class="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                        <button class="btn btn-primary mb-4" type="button"><i class="fas fa-arrow-left"></i> Back
                        </button>
                        <h2 class="mb-2 text-truncate">Der Lehrer</h2>
                        <h4 class="mb-4">-Staffel 2-</h4>
                        <div class="progress">
                            <div class="progress-bar" role="progressbar" style="width: 28%;" aria-valuenow="28"
                                 aria-valuemin="0" aria-valuemax="100">2/7
                            </div>
                        </div>
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
