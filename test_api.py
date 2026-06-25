import urllib.request
import json

data = json.dumps({
    "email": "test1234567@gmail.com",
    "phone": "01234567892",
    "password": "password123",
    "fullName": "Test User",
    "role": "PATIENT"
}).encode('utf-8')

req = urllib.request.Request('https://medical-server.zeabur.app/api/auth/register', data=data, headers={'Content-Type': 'application/json'})
try:
    with urllib.request.urlopen(req) as res:
        response_body = res.read()
        print("Register response:", response_body)
        token = json.loads(response_body)['token']
        print("Token:", token)
        
        req2 = urllib.request.Request('https://medical-server.zeabur.app/api/users/profile', data=b'{"fullName":"Test"}', headers={'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token}, method='PUT')
        try:
            with urllib.request.urlopen(req2) as res2:
                print("Update profile response:", res2.read())
        except urllib.error.HTTPError as e:
            print("Update profile error:", e.code, e.read())
except urllib.error.HTTPError as e:
    print("Register error:", e.code, e.read())
