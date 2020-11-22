import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Connection, Repository } from 'typeorm';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { User } from './entities/user.entity';

@Injectable()
export class UsersService {
  private readonly users: User[] = [
    {
      id: 1,
      gender: 'male',
      firstName: 'Mike',
      lastName: 'Jordan',
      email: 'mike.jordsky@test.fr',
      password: 'miketest',
      phoneNumber: '06123456789',
      dateOfBirth: '20/07/1995',
      isPremium: false,
      isCustomer: true,
      isProvider: false,
    },
    {
      id: 2,
      gender: 'male',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@test.fr',
      password: 'johnjohn',
      phoneNumber: '06125056789',
      dateOfBirth: '01/01/1999',
      isPremium: false,
      isCustomer: true,
      isProvider: false,
    },
  ];

  constructor(
    @InjectRepository(User)
    private usersRepository: Repository<User>,private connection: Connection
  ) {

  }

  
  create(createUserDto: CreateUserDto) {
    // this.users.push(createUserDto);
    return 'This action adds a new user';
  }
  
  createRepository(createUserDto: CreateUserDto) {
    this.usersRepository.insert(createUserDto);
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

  
  findAllRepository(): Promise<User[]> {
    return this.usersRepository.find();
  }

  findOnRepositorye(id: string): Promise<User> {
    return this.usersRepository.findOne(id);
  }

  async removeRepository(id: string): Promise<void> {
    await this.usersRepository.delete(id);
  }
}
