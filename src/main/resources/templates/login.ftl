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
        <#import "navbar.ftl" as navbar>
        <@navbar.navbar/>

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
