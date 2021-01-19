<!doctype html>
<html lang="en">
    <head>
        <#import "spring.ftl" as s/>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="<@s.url "/webjars/bootstrap/5.0.0-beta1/css/bootstrap.min.css"/>" rel="stylesheet"/>
        <script src="<@s.url "/webjars/bootstrap/5.0.0-beta1/js/bootstrap.bundle.min.js"/>"></script>

        <title>PocketTracker - Login</title>

        <link href="<@s.url "/css/login.css"/>" rel="stylesheet">
    </head>

    <body class="bg-light">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">
                    <img src="<@s.url "/image/PocketTracker.png"/>" alt="" width="35" height="35" class="d-inline-block align-top">
                    PocketTracker - v1.0.0
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
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
            <div class="form-signin align-middle card">
                <form>
                    <img class="mb-4" src="<@s.url "/image/PocketTracker.png"/>" alt="" width="80" height="80">
                    <h1 class="h3 mb-3 fw-normal">PocketTracker</h1>
                    <label for="inputUserName" class="visually-hidden">Username</label>
                    <input type="text" id="inputUserName" class="form-control" placeholder="Username" required autofocus>
                    <label for="inputPassword" class="visually-hidden">Password</label>
                    <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
                    <div class="checkbox mb-3">
                        <label>
                            <input type="checkbox" value="remember-me"> Remember me
                        </label>
                    </div>
                    <button class="w-100 btn btn-lg btn-primary" type="submit">Login</button>
                </form>
            </div>
        </main>
    </body>
</html>
