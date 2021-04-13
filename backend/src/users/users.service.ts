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
    private usersRepository: Repository<User>,
    private connection: Connection,
  ) {}

  create(createUserDto: CreateUserDto) {
    // this.users.push(createUserDto);
    console.log('This action adds a new user');
    return 'This action adds a new user';
  }

  async createRepository(createUserDto: CreateUserDto): Promise<CreateUserDto> {
    return await this.usersRepository.save(createUserDto)
  }

  findAll() {
    console.log('This action returns all users');
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

  /**
   * Returns the list of all users in database
   */
  findAllRepository(): Promise<User[]> {
    return this.usersRepository.find();
  }

  async findOneRepositoryByID(id: number): Promise<User> {
    try {
      return await this.usersRepository.findOne(id);
    } catch (error) {
      console.error(error);
    }
  }

  async findOneRepositoryByEmail(
    email: string,
    password: string,
  ): Promise<User> {
    console.log(`findOneRepositoryByEmail()`);

    console.log(`paramter value :`);
    console.log(email);
    console.log(password);
    // Store response in variable
    const user = await this.usersRepository.findOne({
      where: {
        email: email,
        password: password,
      },
    });

    console.log(`${user.email}`);

    // Check variable's validity (undefined or not)
    if (user.email !== email) {
      throw Error();
    }
    return user;
  }

  async removeRepository(id: string): Promise<void> {
    await this.usersRepository.delete(id);
  }
}
