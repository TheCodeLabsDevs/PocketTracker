<!doctype html>
<html>
    <head>
        <#import "spring.ftl" as s/>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css"
              integrity="sha512-HK5fgLBL+xu6dm/Ii3z4xhlSUyZgTT9tuc/hSrtw6uzJOvgRr2a9jyxxT1ely+B+xFAmJKVSTbpM/CuL7qxO8w=="
              crossorigin="anonymous"/>

        <link href="<@s.url "/webjars/bootstrap/5.0.0-beta1/css/bootstrap.min.css"/>" rel="stylesheet"/>
        <script src="<@s.url "/webjars/bootstrap/5.0.0-beta1/js/bootstrap.bundle.min.js"/>"></script>

        <title>PocketTracker - Episodes</title>

        <link href="<@s.url "/css/main.css"/>" rel="stylesheet">
    </head>

    <body class="bg-light">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">
                    <img src="<@s.url "/image/PocketTracker.png"/>" alt="" width="35" height="35" class="d-inline-block align-top">
                    PocketTracker - v1.0.0
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    </ul>
                    <form class="d-flex">
                        <input class="form-control me-3" type="search" placeholder="Search" aria-label="Search">
                    </form>
                    <a class="btn btn-primary" href="#">Login</a>
                </div>
            </div>
        </nav>

        <main>
            <div class="container mt-5">
                <div class="row mb-4">
                    <div class="col-sm-12 col-md-8 col-lg-6 mx-auto text-center">
                        <button class="btn btn-primary mb-4" type="button"><i class="fas fa-arrow-left"></i> Back</button>
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
