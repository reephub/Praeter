import {
  Controller,
  Get,
  Post,
  Body,
  Put,
  Param,
  Delete,
  Header,
} from '@nestjs/common';
import { UsersService } from './users.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';

@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  /* @Post()
  @Header('Content-Type', 'application/json')
  create(@Body() createUserDto: CreateUserDto) {
    return this.usersService.create(createUserDto);
  } */

  @Post()
  @Header('Content-Type', 'application/json')
  create(@Body() createUserDto: CreateUserDto) {
    return this.usersService.createRepository(createUserDto);
  }

  /* @Get()
  @Header('Content-Type', 'application/json')
  findAll() {
    return this.usersService.findAll();
  } */


  @Get('db')
  @Header('Content-Type', 'application/json')
  getDb() {
    return this.usersService.canConnect();
  }


  @Get()
  @Header('Content-Type', 'application/json')
  findAll() {
    return this.usersService.findAllRepository();
  }

  @Get(':id')
  @Header('Content-Type', 'application/json')
  findOne(@Param('id') id: string) {
    return this.usersService.findOneRepositorye(id);
  }

  @Put(':id')
  @Header('Content-Type', 'application/json')
  update(@Param('id') id: string, @Body() updateUserDto: UpdateUserDto) {
    return this.usersService.update(+id, updateUserDto);
  }

  @Delete(':id')
  @Header('Content-Type', 'application/json')
  remove(@Param('id') id: string) {
    return this.usersService.remove(+id);
  }
}
