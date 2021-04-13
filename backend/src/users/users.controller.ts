import {
  Body,
  Controller,
  Delete,
  Get,
  Header,
  HttpStatus,
  Param,
  Patch,
  Post,
  Put,
  Res,
  ValidationPipe
} from '@nestjs/common';
import { Response } from 'express';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { UsersService } from './users.service';

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
  create(@Body() createUserDto: CreateUserDto, @Res() res: Response) {
    this.usersService
      .createRepository(createUserDto)
      .then(value => {
        console.log({ value });
        console.log(`succesfully saved in database`);

        res.status(HttpStatus.CREATED).json({
          code: 0,
          message: 'user created',
        });
      })
      .catch(reason => {
        console.log(`Error while insert user in database - ` + reason);

        res.status(HttpStatus.BAD_REQUEST).json({
          code: 1,
          message: 'user not created',
        });
      });
  }

  /* @Get()
  @Header('Content-Type', 'application/json')
  findAll() {
    return this.usersService.findAll();
  } */

  @Get()
  @Header('Content-Type', 'application/json')
  findAll() {
    return this.usersService.findAllRepository();
  }

  /* @Get(':id')
  @Header('Content-Type', 'application/json')
  findOne(@Param('id') id: number) {
    return this.usersService.findOneRepositoryByID(id);
  } */

  @Patch('login')
  @Header('Content-Type', 'application/json')
  login(
    @Body(new ValidationPipe({ transform: true }))
    credentials: { email: string; password: string },
    @Res() res: Response,
  ) {
    if (!credentials.email || !credentials.password) {
      console.log(
        `Cannot perform action, email or password is wrong or missing`,
      );
      res.status(HttpStatus.BAD_REQUEST).end();
    } else {
      this.usersService
        .findOneRepositoryByEmail(credentials.email, credentials.password)
        .then(user => res.status(HttpStatus.OK).json(user))
        .catch(_ => res.status(HttpStatus.NOT_FOUND).end());
    }
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
