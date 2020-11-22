import { User } from './entities/user.entity';
import { Injectable } from '@nestjs/common';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';

@Injectable()
export class UsersService {

  private readonly users: User[] = [
    {
      gender : "male",
     firstName :  "Mike",
     lastName: "Jordan", 
     email:"mike.jordsky@test.fr",
     password :  "miketest", 
     phoneNumber : "06123456789", 
     dateOfBirth : "20/07/1995", 
     isPremium : false,
     isCustomer:  true, 
     isProvider : false
    },
    {
      gender : "male",
     firstName :  "John",
     lastName: "Doe", 
     email:"john.doe@test.fr",
     password :  "johnjohn", 
     phoneNumber : "06125056789", 
     dateOfBirth : "01/01/1999", 
     isPremium : false,
     isCustomer:  true, 
     isProvider : false
    },
  ];

  create(createUserDto: CreateUserDto) {
    this.users.push(createUserDto);
    return 'This action adds a new user';
  }

  findAll() {
    return this.users;
    //return `This action returns all users`;
  }

  findOne(id: number) {
    return `This action returns a #${id} user`;
  }

  update(id: number, updateUserDto: UpdateUserDto) {
    return `This action updates a #${id} user`;
  }

  remove(id: number) {
    return `This action removes a #${id} user`;
  }
}
