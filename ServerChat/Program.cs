using System;
using System.Net;
using System.Net.Sockets;

class Program
{
	/// <summary>
	/// Ввод с клавиатуры
	/// </summary>
	static string input;

	/// <summary>
	/// Выход из цикла в основном потоке
	/// </summary>
	static bool IsExit = false;

	/// <summary>
	/// Основная точка входа в программу
	/// </summary>
	static void Main()
	{
		//Задаем Title ip адресом сервера
		Console.Title = "Server " + Memory.GetLocalIPAddress() + ":" + Memory.port;
		//Пробуем запустить прослушку сервера
		try
		{
			Memory.listener = new TcpListener(IPAddress.Parse(Memory.GetLocalIPAddress()), Memory.port);
			Memory.listener.Start();
			Memory.listening.Start();
			//Сообщаем о запуске
			Console.WriteLine("Server started on " + Memory.GetLocalIPAddress() + ":" + Memory.port);
			while (!IsExit)
			{
				input = Console.ReadLine();
				switch (input)
				{
					case "/clear":
						Console.Clear();
						Console.WriteLine("Server run on " + Memory.GetLocalIPAddress() + ":" + Memory.port);
						break;
					case "/exit":
					case "/stop":
						IsExit = true;
						break;
					case "/users":
						Console.ForegroundColor = ConsoleColor.Cyan;
						Console.WriteLine("Online now: {0}", Memory.clientConnected.Count);
						Console.ResetColor();
						break;
					default:
						Memory.SendAll("Хозяин: " + input, null);
						break;
				}
			}

		}
		catch (Exception ex)
		{
			//Выводим ошибку
			Console.ForegroundColor = ConsoleColor.Red;
			Console.WriteLine("Error starting server:");
			Console.WriteLine(ex);
			Console.WriteLine("Critical Completion");
		}
		finally
		{
			//Отключение сервера
			if (Memory.listener != null)
				Memory.listener.Stop();
			Console.WriteLine("Server is down");
		}
		Environment.Exit(0);
	}
}