using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;
using System.Text;
using System.Net.NetworkInformation;
using System.Linq;

public static class Memory
{
	/// <summary>
	/// Подключенные устройства
	/// </summary>
	public static List<User> clientConnected = new List<User>();
	public static Thread listening = new Thread(new ThreadStart(ListenerAccept));
	public const int port = 8888;

	/// <summary>
	/// TCP слушатель
	/// </summary>
	public static TcpListener listener;

	public static void SendAll(string s, User u)
	{
		for (int i = 0; i < clientConnected.Count; i++)
			if (clientConnected[i] != u)
				clientConnected[i].SendMessage(s);
	}

	/// <summary>
	/// Поток добавления новых пользователей
	/// </summary>
	public static void ListenerAccept()
	{
		try
		{
			while (true)
			{
				TcpClient client = listener.AcceptTcpClient();
				clientConnected.Add(new User(client));
			}
		}
		catch (Exception e)
		{
			Console.WriteLine(e.Message);
		}
	}

	/// <summary>
	/// Gets the local IP Address.
	/// </summary>
	/// <returns>The local IP Address.</returns>
	public static string GetLocalIPAddress()
	{
		foreach (var netI in NetworkInterface.GetAllNetworkInterfaces())
		{
			if (netI.NetworkInterfaceType != NetworkInterfaceType.Wireless80211 &&
				(netI.NetworkInterfaceType != NetworkInterfaceType.Ethernet ||
				 netI.OperationalStatus != OperationalStatus.Up)) continue;
			foreach (var uniIpAddrInfo in netI.GetIPProperties().UnicastAddresses.Where(x => netI.GetIPProperties().GatewayAddresses.Count > 0))
			{

				if (uniIpAddrInfo.Address.AddressFamily == AddressFamily.InterNetwork &&
					uniIpAddrInfo.AddressPreferredLifetime != uint.MaxValue)
					return uniIpAddrInfo.Address.ToString();
			}
		}
		throw new Exception("You local IPv4 address couldn't be found...");
	}
}

public class User
{
	/// <summary>
	/// The name.
	/// </summary>
	public string Name = "";

	/// <summary>
	/// TCP клиент
	/// </summary>
	public TcpClient tcpClient;
	/// <summary>
	/// Поток прослушки
	/// </summary>
	public Thread listening;

	/// <summary>
	/// The stream.
	/// </summary>
	readonly NetworkStream stream;

	/// <summary>
	/// Конструктор
	/// </summary>
	/// <param name="client">TcpClient.</param>
	public User(TcpClient client)
	{
		tcpClient = client;
		try
		{
			stream = tcpClient.GetStream();
			// Буфер для получаемых данных
			byte[] data = new byte[128];

			// Получаем сообщение
			StringBuilder builder = new StringBuilder();
			int bytes = 0;
			do
			{
				bytes = stream.Read(data, 0, data.Length);
				builder.Append(Encoding.UTF8.GetString(data, 0, bytes));
			}
			while (stream.DataAvailable);
			Name = builder.ToString();
			Console.WriteLine(Name + " : connected");
			Memory.SendAll(Name + " : connected", this);
		}
		catch (Exception ex)
		{
			//Вывод ошибки
			Console.WriteLine(ex.Message);
		}
		//Новый поток для обслуживания нового клиента
		listening = new Thread(new ThreadStart(Listening));
		listening.Start();
	}

	/// <summary>
	/// Метод прослушки
	/// </summary>
	public void Listening()
	{
		try
		{
			// Буфер для получаемых данных
			byte[] data = new byte[128];
			while (true)
			{
				// Получаем сообщение
				StringBuilder builder = new StringBuilder();
				int bytes = 0;
				do
				{
					bytes = stream.Read(data, 0, data.Length);
					builder.Append(Encoding.UTF8.GetString(data, 0, bytes));
				}
				while (stream.DataAvailable);
				if (builder.Length == 0) break;
				string message = builder.ToString();
				Console.WriteLine(Name + " : " + message);
				Memory.SendAll(Name + " : " + message, this);
			}
		}
		catch (Exception ex)
		{
			//Вывод ошибки
			Console.WriteLine(ex.Message);
		}
		finally
		{
			//Закрытие моста
			if (stream != null)
				stream.Close();
			//Отключение прослушивания
			if (tcpClient != null)
				tcpClient.Close();
			//Удаление из списка пользователей
			Console.WriteLine(Name + " : disconnected");
			Memory.clientConnected.Remove(this);
			Memory.SendAll(Name + " : disconnected", this);
		}
	}

	/// <summary>
	/// Отправить сообщение клиенту
	/// </summary>
	/// <param name="message">Текст</param>
	public void SendMessage(string message) => stream.Write(Encoding.UTF8.GetBytes(message), 0, Encoding.UTF8.GetBytes(message).Length);

	/// <summary>
	/// Отправка данных клиенту
	/// </summary>
	/// <param name="message">byte[] данные</param>
	public void SendMessage(byte[] message) => stream.Write(message, 0, message.Length);
}