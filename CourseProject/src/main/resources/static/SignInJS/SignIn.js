document.getElementById('SignIn').addEventListener('submit',async function (e){
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password =document.getElementById('password').value;
    if(ValidateEmail(email)&&ValidatePassword(password,false)){
        const response = await fetch('/api/auth/signin',{
            method:"POST",
            headers:{
                "Content-type": "application/json",
                "Accept": "application/json"
            },
            body:JSON.stringify({
                "email":email,
                "password":password
            })
        });
        if(response.ok){
            let responseJson = await response.json();
            if(responseJson.exception===null||responseJson.exception==''){
                JwtCookie('jwt',responseJson.token,2);
                await fetch('NewVisit',{
                    method:"PATCH"
                })
                if(responseJson.role==='ROLE_Admin')
                    window.location.href = '/AllUsersPage';
                else
                    window.location.href = '/UserMainPage'
            }
            else{
                console.log('Error');
                HandleError(responseJson.exception);
            }
        }
        else{
            const error = await response.json();
            console.log(error);
        }
    }
})